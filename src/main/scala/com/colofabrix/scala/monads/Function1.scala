package com.colofabrix.scala.monads

/**
  * Mimicking the Function1 object
  */
object Function1 {

  /**
    * Implicit type converter for Monoid[A => B]
    * Implements the real Monoid behaviour for A => B
    */
  implicit def functionMonoid[A, B: Monoid]: Monoid[A => B] = {
    val mb = implicitly[Monoid[B]]

    new Monoid[A => B] {
      override def mempty: A => B = _ => mb.mempty
      override def mappend( x: A => B, y: A => B ): A => B = { a =>
        mb.mappend(x(a), y(a))
      }
    }
  }

}
