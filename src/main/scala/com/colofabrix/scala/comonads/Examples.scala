package com.colofabrix.scala.comonads

import NEL._

object Examples {

  def run(): Unit = {
    println(
      """
        |  ~ ~  COMONADS STUDY  ~ ~
        """.stripMargin
    )

    println( "\n ~ Non Empty List Comonad ~ \n" )

    val testNel =
      NEL("a", Some(
        NEL("b", Some(
          NEL("c", None)
        ))
      ))

    println(s"Extract:\n  ${testNel.extract}")

    println(s"\nDuplicate:\n  ${testNel.duplicate}")

    print("\nFolding of elements:\n  ")
    println(testNel.foldLeft("") { (a, x) =>
      a + x.toUpperCase
    })

    print("\nContextual elaboration of each value:\n  ")
    println(testNel.coflatMap { nel =>
      val head = nel.head.toUpperCase()
      val context = (for {
        t <- nel.tail
      } yield {
        t.foldLeft("") { (a, x) => x + a + x }
      }).getOrElse("")

      s"|$context $head $context|"
    })
  }

}
