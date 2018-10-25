package com.colofabrix.scala.monadstudy

trait Monad[M[_]] {
  def bind[A, B]( f: A => M[B] ): M[B]
  def unit[A]( a: A ): M[A]
  def fmap[A, B]( f: A => B ): M[B]
}
