package com.colofabrix.scala.comonads

import NEL._

object Examples {

  def run(): Unit = {
    println(
      """
        |  ~ ~  COMONADS STUDY  ~ ~
        |
        """.stripMargin
    )

    println( "\n ~ Non Empty List Comonad ~ \n" )

    val newNel =
      NEL("a", Some(
        NEL("b", Some(
          NEL("c", None)
        ))
      ))

    println(newNel.extract)
    println(newNel.duplicate)
    println(newNel.coflatMap { nel =>
      nel.head.toUpperCase() + nel.tail.map(_.head)
    })
  }

}
