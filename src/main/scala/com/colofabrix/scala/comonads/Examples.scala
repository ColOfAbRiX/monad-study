package com.colofabrix.scala.comonads

import com.colofabrix.scala.comonads.NEL._
import com.colofabrix.scala.monads.Reader

import scala.util.Random

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
    val nel = NEL("a", NEL("s", NEL("d", NEL("f"))))

    // Comonad.extract
    println(s"Extract:\n  ${nel.extract}")

    // Comonad.foldLeft
    val result1 = nel.foldLeft("") { (a, x) =>
      a + x.toUpperCase
    }
    println(s"\nFolding of elements:\n  $result1")

    // Comonad.map
    val result2 = nel.map(_.hashCode)
    println(s"\nMapping over the comonad:\n  $result2")

    // Comonad.duplicate
    println(s"\nDuplicate:\n  ${nel.duplicate}")

    // Comonad.coflatMap
    val result3 = nel.coflatMap { nel =>
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
    val configLengthFunc: String => String = { c =>
      s"Config length: ${c.length} characters"
    }
    val configUppercaseFunc: String => String = _.toUpperCase

    println(s"Configuration:\n  $config")

    val result5 = Reader( configLengthFunc ).run( config )
    println(s"\nReader monad result:\n  $result5")


    println( "\n ~ And then reversing it with the Product comonad ~ \n" )

    val result6 = Product( config, configLengthFunc(config) )
      .coflatMap { outer =>
        Product( config, configUppercaseFunc(config) )
          .coflatMap { inner =>
            s"Result: ${outer.extract} + ${inner.extract}"
          }
      }

    println( result6 )


    //
    // Store Comonad
    //

    println( "\n ~ Using a store comonad ~ \n" )

    // List of random numbers
    val randoms = List.fill( 10 ) {
      new Random().nextInt( 10 )
    }
    println( s"\nRandom list:\n  $randoms" )

    // Function to access the store as a ring
    val accessFunc: Int => Int = { i =>
      val index = if( i >= 0 ) {
        i % randoms.length
      } else {
        (randoms.length - i - 2) % (-randoms.length)
      }
      randoms(index)
    }

    // Function that calculates the average of the immediate neighbours of a
    // given cursor
    val averageFunc: Store[Int, Int] => Int = { s =>
      List(-1, 0, 1)
        .map( d =>
          s.seek( s.cursor + d ).extract
        ).sum / 3
    }

    // Build a smoothed list
    val smoothed = randoms.map { i =>
      Store( accessFunc, i )
        .coflatMap(averageFunc)
        .extract
    }
    println( s"\nSmoothed list:\n  $smoothed")

    println("")
  }

}
