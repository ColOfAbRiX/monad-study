package com.colofabrix.scala.comonads

/**
  * Product comonad, aka CoReaader (with simplified internal structure for simplicity)
  */
case class Product[R, A]( ask: R, value: A ) {
  override def toString: String = {
    val h = ask.toString.replaceAll("\n", "\n  ")
    val r = extract.toString.replaceAll("\n", "\n  ")
    s"Product(\n  $h,\n  $r\n)"
  }

  def extract: A =
    value

  def duplicate: Product[R, Product[R, A]] =
    Product( this.ask, this )

  def map[B]( f: A => B ): Product[R, B] =
    Product( this.ask, f(this.extract) )

  def coflatMap[B]( f: Product[R, A] => B ): Product[R, B] =
    duplicate map f
}
