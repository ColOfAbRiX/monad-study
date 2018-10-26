package com.colofabrix.scala.monadstudy

trait Monoid[A] {
  def mempty: A
  def mappend(x: A, y: A): A
}

trait Functor[M[_], A] {
  def fmap[B]( f: A => B ): M[B]
}

trait Monad[M[_], A] extends Functor[M, A] {
  def bind[B]( f: A => M[B] ): M[B]
  def unit( a: A ): M[A]
}

//class Writer[A, W: Monoid[_]] extends Monad[Writer, A]
