package com.colofabrix.scala.monads

import com.colofabrix.scala.generics.{ Functor, Monad, Monoid }

/**
  * Option Algebraic Data Type
  */
sealed trait Option[+A]
case object None extends Option[Nothing]
case class Some[A]( value: A ) extends Option[A]

object Option {

  /**
    * Extending the base Option to add Monad and Monoid methods.
    * It's an incorporation of the below implicits into the Option ADT
    */
  implicit class OptionOps[A]( o: Option[A] ) {
    def map[B]( f: A => B )( implicit fo: Functor[Option] ) : Option[B] =
      fo.fmap( o )( f )

    def flatMap[B]( f: A => Option[B] )( implicit mo: Monad[Option] ): Option[B] =
      mo.bind( o )( f )

    def empty( implicit mf: Monoid[Option[A]] ): Option[A] =
      mf.mempty

    def ++( that: Option[A] )( implicit mf: Monoid[Option[A]] ): Option[A] =
      mf.mappend( o, that )
  }

  /**
    * Type class instance for Monoid[Option]
    * Implements the real Monoid behaviour for Option
    */
  implicit def optionMonoid[A: Monoid]: Monoid[Option[A]] = {
    val ma = implicitly[Monoid[A]]

    new Monoid[Option[A]] {
      override def mempty: Option[A] = None

      override def mappend( x: Option[A], y: Option[A] ): Option[A] = {
        (x, y) match {
          case (a, None) => a
          case (None, b) => b
          case (Some(a), Some(b)) => Some(ma.mappend(a, b))
        }
      }
    }
  }

  /**
    * Type class instance for Monad[Option]
    * Implements the real Monad behaviour for Option
    */
  implicit val optionMonad: Monad[Option] = new Monad[Option] {
    override def unit[A]( a: A ): Option[A] = Some(a)

    override def bind[A, B]( ma: Option[A] )( f: A => Option[B] ): Option[B] = {
      ma match {
        case Some(a) => f(a)
        case None => None
      }
    }
  }

}
