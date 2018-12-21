package com.colofabrix.scala.generics

/**
  * Monoid
  * See: https://www.reddit.com/r/scala/comments/45gqpd/whats_a_monoid/
  */
trait Monoid[A] {
  def mempty: A
  def mappend( x: A, y: A ): A
}
