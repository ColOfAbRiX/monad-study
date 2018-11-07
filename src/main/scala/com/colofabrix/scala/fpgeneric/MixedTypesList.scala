package com.colofabrix.scala.fpgeneric

/**
  * Mimicking the List object
  */
object List {

  implicit def listMonoid[A]: Monoid[List[A]] = {
    new Monoid[List[A]] {
      override def mempty: List[A] = Nil
      override def mappend( x: List[A], y: List[A] ): List[A] = x ::: y
    }
  }

  implicit val listMonad: Monad[List] = new Monad[List] {
    override def unit[A]( a: A ): List[A] = a :: Nil
    override def bind[A, B]( ma: List[A] )( f: A => List[B] ): List[B] = ma.flatMap(f)
  }

}

/**
  * Mimicking the String object
  */
object String {

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    override def mempty: String = ""
    override def mappend( x: String, y: String ): String = x + y
  }

}

/**
  * Mimicking the Function1 object
  */
object Function1 {

  implicit def functionMonoid[A, B: Monoid]: Monoid[A => B] = {
    val mb = implicitly[Monoid[B]]
    new Monoid[A => B] {
      override def mempty: A => B = _ => mb.mempty
      override def mappend( x: A => B, y: A => B ): A => B = { a => mb.mappend(x(a), y(a)) }
    }
  }

}
