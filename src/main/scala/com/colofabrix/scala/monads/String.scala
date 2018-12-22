package com.colofabrix.scala.monads

import com.colofabrix.scala.generics.Monoid

/**
  * Mimicking the String object
  */
object String {

  /**
    * Type class instance for Monoid[String]
    * Implements the real Monoid behaviour for String
    */
  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    override def mempty: String = ""
    override def mappend( x: String, y: String ): String = x + y
  }

}
