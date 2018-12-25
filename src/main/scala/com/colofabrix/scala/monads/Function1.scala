package com.colofabrix.scala.monads

import com.colofabrix.scala.generics.Monoid

/**
  * Mimicking the Function1 object
  */
object Function1 {

  /**
    * Extending the base Function1 to add Monoid methods.
    * It's an incorporation of the below implicits into the Function1 ADT
    */
  implicit class Function1Ops[A, B]( func: A => B ) {
    def empty( implicit mf: Monoid[A => B] ): A => B =
      mf.mempty

    def ++( that: A => B )( implicit mf: Monoid[A => B] ): A => B =
      mf.mappend( func, that )
  }

  /**
    * Type class instance for Monoid[A => B]
    * Implements the real Monoid behaviour for A => B
    */
  implicit def functionMonoid[A, B: Monoid]: Monoid[A => B] = new Monoid[A => B] {
    private val mb = implicitly[Monoid[B]]

    override def mempty: A => B = _ => mb.mempty

    override def mappend( x: A => B, y: A => B ): A => B = { a =>
      mb.mappend( x(a), y(a) )
    }
  }

}
