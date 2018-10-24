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
    ( newSeed, newRandom )
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
    * EXAMPLE #3
    * Introducing the concept of Action. Instead of working with input and output values I
    * work with functions that perform the requested operations. I can think of
    * returning an "action" that will perform what I need.
    */
  def random3(): Int => (Int, Double) = { seed =>
    val newRandom = new Random( seed ).nextDouble()
    val newSeed = (newRandom * Int.MaxValue).toInt
    ( newSeed, newRandom )
  }

  def example3( x: Int ): Unit = {
    // First random number, new seed computed automatically
    val n1 = random3()( x )
    // Second random number, new seed computed automatically
    val n2 = random3()( n1._1 )

    // Compute result
    val result = (n1._2 + n2._2) / 2.0

    println( "EXAMPLE #3 - Introducing the concept of Action" )
    println( s"Result: $result" )
    println( "" )
  }

  /**
    * EXAMPLE #4
    * Adding a plumbing function: I don't want the caller to manage the binding
    * of the functions, it should only use them, so I create a new plumbing
    * function that does exactly that, it binds together function calls.
    */
  def random4(): Int => (Int, Double) = { seed =>
    val newRandom = new Random( seed ).nextDouble()
    val newSeed = (newRandom * Int.MaxValue).toInt
    ( newSeed, newRandom )
  }

  def bind4( g: Double => Int => (Int, Double) )( f: Int => (Int, Double) ): Int => (Int, Double) = { seed =>
    val (newSeed, newRandom) = f( seed )
    g(newRandom)(newSeed)
  }

  def unit4( a: Double ): Int => (Int, Double) = { seed => (seed, a) }

  def example4( x: Int ): Unit = {
    // Create a computation that will calculate the result
    val computation = bind4 { n1 =>
      bind4 { n2 =>
        unit4((n1 + n2) / 2.0)
      }( random4() )
    }( random4() )

    // Compute the result
    val result = computation( x )

    println( "EXAMPLE #4 - Adding a plumbing function" )
    println( s"Result: ${result._2}" )
    println( "" )
  }

  /**
    * EXAMPLE #5
    * Renaming and cleaning: use better names, use currying and explicit the
    * output data structures so that it becomes more practical to use the
    * binding function.
    */
  type RNG5 = Int => (Int, Double)

  def random5(): RNG5 = { seed =>
    val newRandom = new Random( seed ).nextDouble()
    val newSeed = (newRandom * Int.MaxValue).toInt
    ( newSeed, newRandom )
  }

  def bind5( g: Double => RNG5 )( f: RNG5 ): RNG5 = { seed =>
    val (newSeed, newRandom) = f( seed )
    g( newRandom )( newSeed )
  }

  def unit5( a: Double ): RNG5 = { seed => (seed, a) }

  def example5( x: Int ): Unit = {
    // Create a computation that will calculate the result
    val computation = bind4 { n1 =>
      bind4 { n2 =>
        unit4((n1 + n2) / 2.0)
      }( random4() )
    }( random4() )

    // Compute the result
    val result = computation( x )

    println( "EXAMPLE #5 - Renaming and cleaning" )
    println( s"Result: ${result._2}" )
    println( "" )
  }

  /**
    * EXAMPLE #6
    * Explicit State monad: create a class that incorporates the generic
    * functionalities developed in the previous example and that makes the
    * usage of these functionalities even more practical.
    */
  import Randomised6._

  class Randomised6( f: RNG6 ) {
    def bind( g: Double => RNG6 ): RNG6 = { seed =>
      val (newSeed, newRandom) = f( seed )
      g( newRandom )( newSeed )
    }
  }

  object Randomised6 {
    type RNG6 = Int => (Int, Double)
    def apply( a: Double ): RNG6 = { seed => (seed, a) }
    def apply( f: RNG6 ): Randomised6 = new Randomised6( f )
  }

  def random6(): RNG6 = { seed =>
    val newRandom = new Random( seed ).nextDouble()
    val newSeed = (newRandom * Int.MaxValue).toInt
    ( newSeed, newRandom )
  }

  def example6( x: Int ): Unit = {
    val computation = Randomised6( random6() ).bind { n1 =>
      Randomised6( random6() ).bind { n2 =>
        Randomised6( (n1 + n2) / 2.0 )
      }
    }

    // Compute the result
    val result = computation( x )

    println( "EXAMPLE #6 - Explicit State monad" )
    println( s"Result: ${result._2}" )
    println( "" )
  }

  /**
    * EXAMPLE #7
    * Scala idiomatic way of monads: Scala allows to use a special syntax with
    * a for-expression to work with monads in a nicer way. And usability is
    * improved even more.
    */
  import Randomised7._

  class Randomised7( f: RNG7 ) {
    def flatMap( g: Double => RNG7 ): RNG7 = { seed =>
      val (newSeed, newRandom) = f( seed )
      g( newRandom )( newSeed )
    }
    def map( g: Double => Double ): RNG7 = { seed =>
      val (newSeed, newRandom) = f( seed )
      ( newSeed, g(newRandom) )
    }
  }

  object Randomised7 {
    type RNG7 = Int => (Int, Double)
    def apply( a: Double ): RNG7 = { seed => (seed, a) }
    def apply( f: RNG7 ): Randomised7 = new Randomised7( f )
  }

  def random7(): RNG7 = { seed =>
    val newRandom = new Random( seed ).nextDouble()
    val newSeed = (newRandom * Int.MaxValue).toInt
    ( newSeed, newRandom )
  }

  def example7( x: Int ): Unit = {
    val computation = for {
      n1 <- Randomised7(random7())
      n2 <- Randomised7(random7())
    } yield {
      (n1 + n2) / 2.0
    }

    // Compute the result
    val result = computation( x )

    println( "EXAMPLE #7 - Explicit State monad" )
    println( s"Result: ${result._2}" )
    println( "" )
  }

  /**
    * EXAMPLE #8
    * Introduce generics: use generic types for the Writer monad so that it can
    * be used in different contexts and with more than just logs
    */
  import State._

  class State[S, A]( f: Action[S, A] ) {
    def flatMap( g: A => Action[S, A] ): Action[S, A] = { s =>
      val (ns, x) = f( s )
      g( x )( ns )
    }
    def map( g: A => A ): Action[S, A] = { s =>
      val (ns, x) = f( s )
      ( ns, g(x) )
    }
  }

  object State {
    type Action[S, A] = S => (S, A)
    def apply[S, A]( a: A ): Action[S, A] = { seed => (seed, a) }
    def apply[S, A]( f: Action[S, A] ): State[S, A] = new State( f )
  }

  def random8(): Action[Int, Double] = { seed =>
    val newRandom = new Random( seed ).nextDouble()
    val newSeed = (newRandom * Int.MaxValue).toInt
    ( newSeed, newRandom )
  }

  def example8( x: Int ): Unit = {
    val computation = for {
      n1 <- State(random8())
      n2 <- State(random8())
    } yield {
      (n1 + n2) / 2.0
    }

    // Compute the result
    val result = computation( x )

    println( "EXAMPLE #8 - Explicit State monad" )
    println( s"Result: ${result._2}" )
    println( "" )
  }

}
