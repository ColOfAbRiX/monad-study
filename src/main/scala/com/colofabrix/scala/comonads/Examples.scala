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

    // Comonad.duplicate
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
    val config = "* Sample config *"
    // Function that uses the configuration
    val configLength: String => String = { c =>
      s"Config length: ${c.length} characters"
    }
    val configUppercase: String => String = _.toUpperCase

    println(s"Configuration:\n  $config")

    val result5 = Reader( configLength ).run( config )
    println(s"\nReader monad result:\n  $result5")

    println( "\n ~ And then reversing it with the Product comonad ~ \n" )

    val test = Product( config, configLength(config) )
      .coflatMap { outer =>
        Product( config, configUppercase(config) )
          .coflatMap { inner =>
            s"Result: ${outer.extract} + ${inner.extract}"
          }
      }

    println( test )
    println("")
  }

}
