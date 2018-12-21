package com.colofabrix.scala.generics

/**
  * Functor
  */
trait Functor[F[_]] {
  def fmap[A, B]( fa: F[A] )( f: A => B ): F[B]
}
