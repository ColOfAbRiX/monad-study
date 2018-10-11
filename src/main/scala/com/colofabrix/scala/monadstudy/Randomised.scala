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

    // First random number and seed update
    val n1 = random1( seed )
    seed = (n1 * Int.MaxValue).toInt

    // Second random number and seed update
    val n2 = random1( seed )
    seed = (n2 * Int.MaxValue).toInt

    // Compute result
    val result = (n1 + n2) / 2.0

    println( "EXAMPLE #1 - Imperative style state" )
    println( s"Result: $result" )
    println( "" )
  }

  /**
    * EXAMPLE #2
    * Adding FP-style state: I can't have any side effect so I must
    * return the log as output parameter of the function. The caller will
    * manage this return value.
    */
  def random2( seed: Int ): (Int, Double) = {
    val newRandom = new Random( seed ).nextDouble()
    val newSeed = (newRandom * Int.MaxValue).toInt
    (newSeed, newRandom)
  }

  def example2( x: Int ): Unit = {
    // First random number, new seed computed automatically
    val n1 = random2( x )
    // Second random number, new seed computed automatically
    val n2 = random2( n1._1 )

    // Compute result
    val result = (n1._2 + n2._2) / 2.0

    println( "EXAMPLE #2 - Adding FP-style state" )
    println( s"Result: $result" )
    println( "" )
  }

  /**
    * EXAMPLE #3a
    * Introducing the concept of Action. Instead of working with input and output values I
    * work with functions that perform the requested operations. I can think of
    * returning an "action" that will perform what I need.
    */
  def random3a(): Int => (Int, Double) = { seed =>
    val newRandom = new Random( seed ).nextDouble()
    val newSeed = (newRandom * Int.MaxValue).toInt
    (newSeed, newRandom)
  }

  def example3a( x: Int ): Unit = {
    // First random number, new seed computed automatically
    val n1 = random3a()( x )
    // Second random number, new seed computed automatically
    val n2 = random3a()( n1._1 )

    // Compute result
    val result = (n1._2 + n2._2) / 2.0

    println( "EXAMPLE #3a - Introducing the concept of Action" )
    println( s"Result: $result" )
    println( "" )
  }

  /**
    * EXAMPLE #3b
    * Adding a plumbing function: I don't want the caller to manage the binding
    * of the functions, it should only use them, so I create a new plumbing
    * function that does exactly that, it binds together function calls.
    */
  def random3b(): Int => (Int, Double) = { seed =>
    val newRandom = new Random( seed ).nextDouble()
    val newSeed = (newRandom * Int.MaxValue).toInt
    (newSeed, newRandom)
  }

  def bind3b( g: Double => Int => (Int, Double) )( f: Int => (Int, Double) ): Int => (Int, Double) = { seed =>
    val (newSeed, newRandom) = f(seed)
    g(newRandom)(newSeed)
  }

  def unit3b( a: Double ): Int => (Int, Double) = { seed => (seed, a) }

  def example3b( x: Int ): Unit = {
    // Create a computation that will calculate the result
    val computation = bind3b { n1 =>
      bind3b { n2 =>
        unit3b((n1 + n2) / 2.0)
      }( random3b() )
    }( random3b() )

    // Compute the result
    val result = computation(x)

    println( "EXAMPLE #3 - Adding a plumbing function" )
    println( s"Result: ${result._2}" )
    println( "" )
  }

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
