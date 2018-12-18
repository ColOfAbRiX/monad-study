package com.colofabrix.scala.comonads

import com.colofabrix.scala.monads.Functor

/**
  * Comonad
  */
trait Comonad[W[_]] extends Functor[W] {
  def extract[A]( wa: W[A] ): A
  def duplicate[A]( wa: W[A] ): W[W[A]]
  override def fmap[A, B]( wa: W[A] )( f: A => B ): W[B]

  def extend[A, B]( wa: W[A] )( f: W[A] => B ): W[B] = fmap( duplicate( wa ) )( f )
}
