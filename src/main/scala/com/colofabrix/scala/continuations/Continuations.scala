package com.colofabrix.scala.continuations

object Continuations {

  //
  // The continuation is a function that will accept the return
  // value of whatever is computer before:
  //   My function -> Continuation
  // The continuation returns unit because we have no way of capturing its
  // output. It's actually the continuation that captures the output of all the
  // computations that happen before and, also, we have no way of knowing when
  // it will be called.
  //
  type Cont1[-A] = A => Unit

  //
  // The function that makes the calculation wants to know the continuation
  // that it has to call back (yes, continuations are callbacks!). This
  // function returns unit because it passes its result not to the caller but
  // to the continuation.
  //
  def uppercase1(value: String, continuation: Cont1[String]): Unit = {
    val result = value.toUpperCase()
    continuation(result)
  }

  //
  // Taking into account the fact that the function that calls the continuation
  // can fail and return an error
  //
  type Cont2[-A] = Either[Throwable, A] => Unit

  def sqrt2(n: Double, callback: Cont2[Double]): Unit = {
    val result = Either.cond(
      test  = n >= 0.0,
      right = Math.sqrt(n),
      left  = new IllegalArgumentException("No negatives!")
    )
    callback(result)
  }

  //
  // To be pure I introduce a container to defer the execution of the callback.
  // This container is a wrapper around my function of type xxx => Unit that
  // also accepts the continuation of type Either[A, B] => Unit so that the
  // final type of this parameter is (Either[A, B] => Unit) => Unit).
  //
  final case class Container3[A](unsafeRunAsync: (Either[Throwable, A] => Unit) => Unit)

  //
  // This makes the continuation passing style cleaner too
  //
  def sqrt3(n: Double) = Container3[Double] { callback =>
    val result = Either.cond(
      test  = n >= 0.0,
      right = Math.sqrt(n),
      left  = new IllegalArgumentException("No negatives!")
    )
    callback(result)
  }

  //
  // The container is in fact a monad for which I can write the monad methods
  // (I'm not proving the monad laws, though)
  //
  implicit class Container3Ops[A](self: Container3[A]) {
    def pure[A](value: (Either[Throwable, A] => Unit) => Unit): Container3[A] = Container3(value)

    def map[B](f: A => B): Container3[B] = pure[B] { cb =>
      self.unsafeRunAsync { result =>
        cb(result.map(f))
      }
    }

    def flatMap[B](f: A => Container3[B]): Container3[B] = pure[B] { cb =>
      self.unsafeRunAsync { result =>
        result match {
          case Left(e)  => cb(Left(e))
          case Right(a) => f(a).unsafeRunAsync(cb)
        }
      }
    }
  }

  def triple3(n: Double): Container3[Double] = Container3[Double] { cb =>
    cb(Right(n * 3.0))
  }

  // Final revelation: Container3[A] is in fact the IO monad with async computation

  type Async[A] = Container3[A]

}
