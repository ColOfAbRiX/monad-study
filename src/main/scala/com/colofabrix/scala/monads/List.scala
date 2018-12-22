package com.colofabrix.scala.monads

import com.colofabrix.scala.generics.{ Functor, Monad, Monoid }

/**
  * Mimicking the List object
  */
object List {

  /**
    * Type class instance for Functor[List]
    * Implements the real Monad behaviour for List
    */
  implicit val listFunctor: Functor[List] = new Functor[List] {
    override def fmap[A, B]( fa: List[A] )( f: A => B ): List[B] = fa.map( f )
  }

  /**
    * Type class instance for Monoid[List]
    * Implements the real Monoid behaviour for List
    */
  implicit def listMonoid[A]: Monoid[List[A]] = {
    new Monoid[List[A]] {
      override def mempty: List[A] = Nil
      override def mappend( x: List[A], y: List[A] ): List[A] = x ::: y
    }
  }

  /**
    * Type class instance for Monad[List]
    * Implements the real Monad behaviour for List
    */
  implicit val listMonad: Monad[List] = new Monad[List] {
    override def unit[A]( a: A ): List[A] = a :: Nil
    override def bind[A, B]( ma: List[A] )( f: A => List[B] ): List[B] = ma.flatMap(f)
  }

}
