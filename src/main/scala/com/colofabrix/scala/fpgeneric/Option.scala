package com.colofabrix.scala.fpgeneric

sealed trait Option[+A]
case object None extends Option[Nothing]
case class Some[A]( value: A ) extends Option[A]

object Option {

  /**
    * Extending the base Option to add Monad methods
    */
  implicit class OptionOps[A]( o: Option[A] ) {
    def map[B]( f: A => B )( implicit fo: Functor[Option] ) : Option[B] = {
      fo.fmap( o )( f )
    }
    def flatMap[B]( f: A => Option[B] )( implicit mo: Monad[Option] ): Option[B] = {
      mo.bind( o )( f )
    }
  }

  /**
    * Implicit type converter for Monoid[Option]
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
    * Implicit type converter for Monad[Option]
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
