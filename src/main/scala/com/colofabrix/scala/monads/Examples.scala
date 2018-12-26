package com.colofabrix.scala.monads

object Examples {

  import List._
  import Option._
  import com.colofabrix.scala.monoids.String._
  import Writer._
  import com.colofabrix.scala.generics._

  def run(): Unit = {
    println(
      """
        |  ~ ~  HIGHER KINDED TYPES STUDY  ~ ~
        """.stripMargin
    )

    //
    // Using extension methods to add features to the basic ADT objects without
    // actually changing them.
    //
    println( "\n ~ Testing the methods of the converters ~ \n" )

    val result1 = optionMonad.bind( optionMonad.unit( "Hello" ) ) { a =>
      Some( s"$a, World!" )
    }

    println(result1)


    //
    // Testing the commodity methods to see how I can use type classes to have
    // a better interface to work with objects
    //
    println( "\n ~ Testing the commodity methods ~ \n" )

    val some: List[Option[String]] = Some( "Hello" ) :: Some( ", " ) :: Some( "World" ) :: Some( "!" ) :: Nil
    map(
      bind( fold( some ) ) { x => Some( x.toUpperCase() ) }
    )( println )


    //
    // Using Scala for comprehension with the extension methods to learn how to
    // integrate pure FP methods
    //
    println( "\n ~ Testing the Scala idiomatic code ~ \n" )

    val result3 = for {
      a <- Some("this is")
      b <- Some("a simple uppercase sentence")
    } yield {
      a.toUpperCase + " " + b.toUpperCase
    }

    println(result3)


    //
    // Testing the Writer monad. The writer monad is more complicated because it
    // is a type constructor of 2 types and not just 1 as required to be a monad
    //
    println( "\n ~ Testing Writer monad ~ \n" )

    val result4 = for {
      initial <- Writer[Int, List[String]](12, Nil)
      addition <- Writer(initial + 3, "Added three" :: Nil)
      multiplication <- Writer(addition * 5, "Multiplied by five" :: Nil)
    } yield {
      multiplication
    }

    println(result4)

    println("")
  }

}
