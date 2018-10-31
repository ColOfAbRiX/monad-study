package com.colofabrix.scala.fpgeneric

sealed trait Option[+A]
case object None extends Option[Nothing]
case class Some[A]( value: A ) extends Option[A]

object Option {

  implicit val optionMonad: Monad[Option] = new Monad[Option] {
    override def unit[A]( a: A ): Option[A] = Some(a)
    override def bind[A, B]( ma: Option[A] )( f: A => Option[B] ): Option[B] = {
      ma match {
        case None => None
        case Some(a) => f(a)
      }
    }
  }

  implicit def optionMonoid[A](implicit am: Monoid[A]): Monoid[Option[A]] = {
    new Monoid[Option[A]] {
      override def mempty: Option[A] = None
      override def mappend( x: Option[A], y: Option[A] ): Option[A] = {
        (x, y) match {
          case (a, None) => a
          case (None, b) => b
          case (Some(a), Some(b)) => Some(am.mappend(a, b))
        }
      }
    }
  }

}