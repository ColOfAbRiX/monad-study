package com.colofabrix.scala.monadstudy

import scala.util.Random

object Randomised {

  /**
    * EXAMPLE #1
    * Imperative style state: using a mutable variable to hold a state, updating
    * it and passing it around.
    */
  def random1( seed: Int ): Double = new Random( seed ).nextDouble()

  def example1( x: Int ): Unit = {
    var seed = x
    val r1 = random1( seed )

    seed = (r1 * Int.MaxValue).toInt
    val r2 = random1( seed )

    println( "EXAMPLE #1 - Imperative style state" )
    println( s"Random #1: $r1" )
    println( s"Random #2: $r2" )
    println( "" )
  }

  /**
    * EXAMPLE #2
    * Adding FP-style state: I can't have any side effect so I must
    * return the log as output parameter of the function. The caller will
    * manage this return value.
    */

  def random2( seed: Int ): (Int, Double) = Tuple2(
    new Random( seed ).nextInt(),
    new Random( seed ).nextDouble()
  )

  def example2( x: Int ): Unit = {
    val r1 = random2( x )
    val r2 = random2( r1._1 )

    println( "EXAMPLE #1 - Normal functions composition" )
    println( s"Random #1: ${r1._2}" )
    println( s"Random #2: ${r2._2}" )
    println( "" )
  }

  /**
    * EXAMPLE #3
    * Adding a plumbing function: I don't want the caller to manage the binding
    * of the functions, it should only use them, so I create a new plumbing
    * function that does exactly that, it binds together function calls.
    */

  /**
    * EXAMPLE #4
    * Renaming and cleaning: use better names, use currying and explicit the
    * output data structures so that it becomes more practical to use the
    * binding function.
    */

  /**
    * EXAMPLE #5
    * Explicit Writer monad: create a class that incorporates the generic
    * functionalities developed in the previous example and that makes the
    * usage of these functionalities even more practical.
    */

  /**
    * EXAMPLE #6
    * Scala idiomatic way of monads: Scala allows to use a special syntax with
    * a for-expression to work with monads in a nicer way. And usability is
    * improved even more.
    */

  /**
    * EXAMPLE #7
    * Introduce generics: use generic types for the Writer monad so that it can
    * be used in different contexts and with more than just logs
    */

}
