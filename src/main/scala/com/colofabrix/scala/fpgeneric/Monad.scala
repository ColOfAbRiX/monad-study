package com.colofabrix.scala.fpgeneric

trait Monad[M[_]] extends Functor[M] {
  def unit[A]( a: A ): M[A]
  def bind[A, B]( ma: M[A] )( f: A => M[B] ): M[B]
  def join[A]( mma: M[M[A]] ): M[A] = bind(mma){ ma => ma }
  override def map[A, B]( ma: M[A] )( f: A => B ): M[B] = bind( ma ) { a => unit(f(a)) }
}
