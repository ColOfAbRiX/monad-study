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

    val testNel = NEL("a", NEL("s", NEL("d", NEL("f"))))

    println(s"Extract:\n  ${testNel.extract}")

    print("\nFolding of elements:\n  ")
    println(testNel.foldLeft("") { (a, x) =>
      a + x.toUpperCase
    })

    print("\nMapping over the comonad:\n  ")
    println(testNel.map(_.hashCode))

    println(s"\nDuplicate:\n  ${testNel.duplicate}")

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
