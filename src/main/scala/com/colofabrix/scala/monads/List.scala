package com.colofabrix.scala.monads

import com.colofabrix.scala.generics.{ Monad, Monoid }

/**
  * Mimicking the List object
  */
object List {

  /**
    * Implicit type converter for Monoid[List]
    * Implements the real Monoid behaviour for List
    */
  implicit def listMonoid[A]: Monoid[List[A]] = {
    new Monoid[List[A]] {
      override def mempty: List[A] = Nil
      override def mappend( x: List[A], y: List[A] ): List[A] = x ::: y
    }
  }

  /**
    * Implicit type converter for Monad[List]
    * Implements the real Monad behaviour for List
    */
  implicit val listMonad: Monad[List] = new Monad[List] {
    override def unit[A]( a: A ): List[A] = a :: Nil
    override def bind[A, B]( ma: List[A] )( f: A => List[B] ): List[B] = ma.flatMap(f)
  }

}
