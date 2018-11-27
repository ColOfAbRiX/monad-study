package com.colofabrix.scala.fpgeneric

object Examples {

  import Option._
  import String._

  def run(): Unit = {
    println(
      """
        |  ~ ~  HIGHER KINDED TYPES STUDY  ~ ~
        |
        """.stripMargin
    )

    // Testing the methods of the converters
    optionMonad.bind( optionMonad.unit( "Hello" ) ) { a =>
      Some( s"$a, World!" )
    }

    // Testing the commodity methods
    val some: List[Option[String]] = Some( "Hello" ) :: Some( ", " ) :: Some( " World" ) :: Some( "!" ) :: Nil
    map(
      bind( fold( some ) ) { x => Some( x.toUpperCase() ) }
    )( println )

    // Testing the Scala idiomatic code
    val uppercase = for {
      a <- Some("simple lowercase sentence")
      b <- Some("that terminates now")
    } yield {
      a.toUpperCase + " " + b.toUpperCase
    }
    println(uppercase)

    println("")
  }

}
