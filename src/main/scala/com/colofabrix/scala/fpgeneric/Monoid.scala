package com.colofabrix.scala.fpgeneric

trait Monoid[A] {
  def mempty: A
  def mappend( x: A, y: A ): A
}

object Monoid {

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    override def mempty: String = ""
    override def mappend( x: String, y: String ): String = x + y
  }

  implicit def listMonoid[A]: Monoid[List[A]] = {
    new Monoid[List[A]] {
      override def mempty: List[A] = Nil
      override def mappend( x: List[A], y: List[A] ): List[A] = x ::: y
    }
  }

  def fold[A](xs: List[A])(implicit am: Monoid[A]): A =
    xs.foldLeft(am.mempty)(am.mappend)

}
