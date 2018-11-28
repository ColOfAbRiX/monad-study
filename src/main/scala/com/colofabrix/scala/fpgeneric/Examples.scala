package com.colofabrix.scala.fpgeneric

object Examples {

  import Option._
  import String._
  import Writer._
  import List._

  def run(): Unit = {
    println(
      """
        |  ~ ~  HIGHER KINDED TYPES STUDY  ~ ~
        |
        """.stripMargin
    )

    //
    // Testing the methods of the converters
    //
    println( "\n ~ Testing the methods of the converters ~ \n" )

    val result1 = optionMonad.bind( optionMonad.unit( "Hello" ) ) { a =>
      Some( s"$a, World!" )
    }

    println(result1)

    //
    // Testing the commodity methods
    //
    println( "\n ~ Testing the commodity methods ~ \n" )

    val some: List[Option[String]] = Some( "Hello" ) :: Some( ", " ) :: Some( "World" ) :: Some( "!" ) :: Nil
    val result2 = map(
      bind( fold( some ) ) { x => Some( x.toUpperCase() ) }
    )( println )

    //
    // Testing the Scala idiomatic code
    //
    println( "\n ~ Testing the Scala idiomatic code ~ \n" )

    val result3 = for {
      a <- Some("simple lowercase sentence")
      b <- Some("that terminates now")
    } yield {
      a.toUpperCase + " " + b.toUpperCase
    }

    println(result3)

    //
    // Testing Writer monad
    //
    println( "\n ~ Testing Writer monad ~ \n" )

    val result4 = for {
      a <- Writer[Int, List[String]](0, Nil)
      b <- Writer(a + 3, "Added three" :: Nil)
      c <- Writer(b * 5, "Multiplied by five" :: Nil)
    } yield {
      c
    }

    println(result4)

    println("")
  }

}
