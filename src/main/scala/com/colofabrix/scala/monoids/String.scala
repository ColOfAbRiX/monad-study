package com.colofabrix.scala.monoids

import com.colofabrix.scala.generics.Monoid

/**
  * Mimicking the String object
  */
object String {

  /**
    * Extending the base String to add Monoid methods.
    * It's an incorporation of the below implicits into the String ADT
    */
  implicit class StringOps( func: String ) {
    def empty( implicit mf: Monoid[String] ): String =
      mf.mempty

    def ++( that: String )( implicit mf: Monoid[String] ): String =
      mf.mappend( func, that )
  }

  /**
    * Type class instance for Monoid[String]
    * Implements the real Monoid behaviour for String
    */
  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    override def mempty: String = ""
    override def mappend( x: String, y: String ): String = x + y
  }

}
