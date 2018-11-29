package com.colofabrix.scala.monads

/**
  * Monoid
  * See: https://www.reddit.com/r/scala/comments/45gqpd/whats_a_monoid/
  */
trait Monoid[A] {
  def mempty: A
  def mappend( x: A, y: A ): A
}

/**
  * Functor
  */
trait Functor[F[_]] {
  def fmap[A, B]( fa: F[A] )( f: A => B ): F[B]
}

/**
  * Monad
  */
trait Monad[M[_]] extends Functor[M] {
  def unit[A]( a: A ): M[A]
  def bind[A, B]( ma: M[A] )( f: A => M[B] ): M[B]
  def join[A]( mma: M[M[A]] ): M[A] = bind(mma){ ma => ma }
  override def fmap[A, B]( ma: M[A] )( f: A => B ): M[B] = bind( ma ) { a => unit(f(a)) }
}
