package com.colofabrix.scala.fpgeneric

trait Monoid[A] {
  def mempty: A
  def mappend( x: A, y: A ): A
}

// See: https://www.reddit.com/r/scala/comments/45gqpd/whats_a_monoid/
object Monoid {

  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    override def mempty: String = ""
    override def mappend( x: String, y: String ): String = x + y
  }

  implicit def functionMonoid[A, B: Monoid]: Monoid[A => B] = {
    val mb = implicitly[Monoid[B]]
    new Monoid[A => B] {
      override def mempty: A => B = _ => mb.mempty
      override def mappend( x: A => B, y: A => B ): A => B = { a => mb.mappend(x(a), y(a)) }
    }
  }

}
