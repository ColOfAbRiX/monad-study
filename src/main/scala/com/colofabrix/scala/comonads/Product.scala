package com.colofabrix.scala.comonads

import com.colofabrix.scala.generics.Comonad

/**
  * Product comonad, aka CoReaader, aka Env
  */
case class Product[R, A]( ask: R, extract: A ) {
  override def toString: String = {
    val h = ask.toString.replaceAll("\n", "\n  ")
    val r = extract.toString.replaceAll("\n", "\n  ")
    s"Product(\n  $h,\n  $r\n)"
  }
}

object Product {

  /**
    * Extending the base Product to add Comonad methods.
    * It's an incorporation of the below implicits into the Id ADT
    */
  implicit class ProductOps[R, A]( product: Product[R, A] ) {
    def extract( implicit wp: Comonad[Product[R, ?]] ): A =
      wp.extract( product )

    def duplicate( implicit wp: Comonad[Product[R, ?]] ): Product[R, Product[R, A]] =
      wp.duplicate( product )

    def coflatMap[B]( f: Product[R, A] => B )( implicit wp: Comonad[Product[R, ?]] ): Product[R, B] =
      wp.extend( product )( f )

    def map[B]( f: A => B )( implicit wp: Comonad[Product[R, ?]] ): Product[R, B] =
      wp.fmap( product )( f )
  }

  /**
    * Type class instance for Comonad[Product]
    * Implements the real Comonad behaviour for Product
    */
  implicit def comonadProduct[R] =
    new Comonad[Product[R, ?]] {

    override def extract[A]( wa: Product[R, A] ): A =
      wa.extract

    override def duplicate[A]( wa: Product[R, A] ): Product[R, Product[R, A]] =
      Product( wa.ask, wa )

    override def fmap[A, B]( wa: Product[R, A] )( f: A => B ): Product[R, B] =
      Product( wa.ask, f(wa.extract) )
  }

}
