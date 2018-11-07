package com.colofabrix.scala.fpgeneric

object Examples {

  import Option._
  import String._

  // A wrapper to simplify the usage of folding on monoids
  def fold[A: Monoid]( xs: List[A] ): A = {
    val ma = implicitly[Monoid[A]]
    xs.foldLeft( ma.mempty )( ma.mappend )
  }

  // A wrapper to simplify the usage of bind on monads
  def bind[M[_]: Monad, A, B]( x: M[A] )( f: A  => M[B] ): M[B] = {
    implicitly[Monad[M]].bind(x)(f)
  }

  // A wrapper to simplify the usage of bind on monads
  def unit[M[_]: Monad, A]( a: A ): M[A] = {
    implicitly[Monad[M]].unit(a)
  }

  // A wrapper to simplify the usage of map on functors
  def map[M[_]: Functor, A, B]( x: M[A] )( f: A => B ): M[B] = {
    implicitly[Functor[M]].map(x)(f)
  }

  def run(): Unit = {
    println(
      """
        |  ~ ~  HIGHER KINDED TYPES STUDY  ~ ~
        |
        """.stripMargin
    )

    // Testing the methods of the converters
    optionMonad.bind( optionMonad.unit( "Hello" ) ) { a =>
      Some( s"$a, World!" )
    }

    // Testing the commodity methods
    val some: List[Option[String]] = Some( "Hello" ) :: Some( ", " ) :: Some( " World" ) :: Some( "!" ) :: Nil
    map(
      bind( fold( some ) ) { x => Some( x.toUpperCase() ) }
    )( println )

    // Testing the Scala idiomatic code
    val uppercase = for {
      a <- Some("simple lowercase sentence")
      b <- Some("that terminates now")
    } yield {
      a.toUpperCase + " " + b.toUpperCase
    }
    println(uppercase)

    println("")
  }

}
