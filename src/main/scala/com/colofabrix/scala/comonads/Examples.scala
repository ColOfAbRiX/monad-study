package com.colofabrix.scala.comonads

import com.colofabrix.scala.comonads.NEL._
import com.colofabrix.scala.monads.Reader

object Examples {

  def run(): Unit = {
    println(
      """
        |  ~ ~  COMONADS STUDY  ~ ~
        """.stripMargin
    )

    //
    // Non Empty List comonad
    //
    println( "\n ~ Non Empty List Comonad ~ \n" )

    // Test NEL
    val testNel = NEL("a", NEL("s", NEL("d", NEL("f"))))

    // Comonad.extract
    println(s"Extract:\n  ${testNel.extract}")

    // Comonad.foldLeft
    val result1 = testNel.foldLeft("") { (a, x) =>
      a + x.toUpperCase
    }
    println(s"\nFolding of elements:\n  $result1")

    // Comonad.map
    val result2 = testNel.map(_.hashCode)
    println(s"\nMapping over the comonad:\n  $result2")

    // Comonad.dupliate
    println(s"\nDuplicate:\n  ${testNel.duplicate}")

    // Comonad.coflatMap
    val result3 = testNel.coflatMap { nel =>
      val head = nel.head.toUpperCase()
      val context = (for {
        t <- nel.tail
      } yield {
        t.foldLeft("") { (a, x) => x + a + x }
      }).getOrElse("")

      s"|$context $head $context|"
    }
    println(s"\nContextual elaboration of each value:\n  $result3")

    //
    // Product comonad (dual of Reader monad)
    //

    println( "\n ~ Using a reader monad ~ \n" )

    // "Configuration" that will be used by the Reader monad
    val config = " * Sample configuration for the Reader monad * "
    // Function that uses the configuration
    val elaboration: String => String = { c =>
      s"The length of the config is: ${c.length} characters"
    }

    println(s"Configuration:\n  $config")

    val result4 = Reader( elaboration ).run( config )
    println(s"\nReader monad result:\n  $result4")

    println( "\n ~ And then reversing it with the Product comonad ~ \n" )

    val test = Product( config, elaboration(config) )
      .coflatMap { x => println(x); x }

    println( test )
    println("")
  }

}
