package com.colofabrix.scala.fpgeneric

object Examples extends App {

  import Option._
  import Monoid._

  def fold[A: Monoid]( xs: List[A] ): A = {
    val ma = implicitly[Monoid[A]]
    xs.foldLeft( ma.mempty )( ma.mappend )
  }

  def bind[M[_]: Monad, A, B]( x: M[A] )( f: A => M[B] ): M[B] = {
    implicitly[Monad[M]].bind(x)(f)
  }

  def map[M[_]: Functor, A, B]( x: M[A] )( f: A => B ): M[B] = {
    implicitly[Functor[M]].map(x)(f)
  }

  optionMonad.bind(optionMonad.unit("Hello")) { a =>
    Some(s"$a, World!")
  }

  val some: List[Option[String]] = Some("Hello") :: Some(", ") :: Some(" World") :: Some("!") :: Nil
  map(
    bind(fold(some)) { x => Some(x.toUpperCase()) }
  )(println)

}
