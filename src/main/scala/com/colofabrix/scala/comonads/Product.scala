package com.colofabrix.scala.comonads

import com.colofabrix.scala.generics.Comonad

/**
  * Product comonad, aka CoReaader
  */
case class Product[R, A]( ask: R, extract: A )

object Product {

  /**
    * Extending the base Product to add Comonad methods.
    * It's an incorporation of the below implicits into the Id ADT
    */
  implicit class ProductOps[R, A]( product: Product[R, A] ) {
    type ProductS[S] = Product[S, A]

    def extract( implicit wp: Comonad[ProductS] ): A =
      wp.extract( product )

    def duplicate( implicit wp: Comonad[Product] ): ProductS[ProductS[A]] =
      wp.duplicate( product )

    def coflatMap[B]( f: ProductS[A] => B )( implicit wp: Comonad[Product] ): ProductS[B] =
      wp.extend( product )( f )

    def map[B]( f: A => B )( implicit wp: Comonad[Product] ): ProductS[B] =
      wp.fmap( product )( f )
  }

  implicit def comonadProduct[R] =
    new Comonad[Product[R, ?]] {

    override def extract[A]( wa: Product[R, A] ): A =
      wa.extract

    override def duplicate[A]( wa: Product[R, A] ): Product[R, Product[R, A]] =
      ???

    override def fmap[A, B]( wa: Product[R, A] )( f: A => B ): Product[R, B] =
      ???
  }

}
