package com.colofabrix.scala.monadstudy

object Debuggable {

  /**
   * EXAMPLE #1
   * Normal functions composition: calling one function and passing its result
   * to another one.
   */
  def outer1( x: Double ): Double = Math.log(x)

  def inner1( x: Double ): Double = Math.exp(x)

  def example1( x: Double ): Unit = {
    val y = outer1( inner1( x ) )

    println( "EXAMPLE #1 - Normal functions composition" )
    println( s"Input:  $x" )
    println( s"Result: $y" )
    println( "" )
  }

  /**
   * EXAMPLE #2
   * Adding some FP-style debugging: I can't have any side effect so I must
   * return the log as output parameter of the function. The caller will
   * manage this return value.
   */
  def outer2( x: Double ): ( Double, String ) = ( Math.log(x), s"Called outer2($x)" )

  def inner2( x: Double ): ( Double, String ) = ( Math.exp(x), s"Called inner2($x)" )

  def example2( x: Double ): Unit = {
    val ( y1, msg1 ) = inner2( x )
    val ( y, msg2 ) = outer2( y1 )
    val msg = msg1 + "\n  " + msg2

    println( "EXAMPLE #2 - Adding some FP-style debugging" )
    println( s"Input:  $x" )
    println( s"Result: $y" )
    println( s"Log:\n  $msg" )
    println( "" )
  }

  /**
   * EXAMPLE #3
   * Adding a plumbing function: I don't want the caller to manage the binding
   * of the functions, it should only use them, so I create a new plumbing
   * function that does exactly that, it binds together function calls.
   */
  def outer3( x: Double ): ( Double, String ) = ( Math.log(x), s"Called outer3($x)" )

  def inner3( x: Double ): ( Double, String ) = ( Math.exp(x), s"Called inner3($x)" )

  def bind3( f: Double => ( Double, String ) )( x: ( Double, String ) ): ( Double, String ) = {
    f( x._1 ) match {
      case ( fx, msg ) => ( fx, x._2 + "\n  " + msg )
    }
  }

  def unit3( x: Double ): ( Double, String ) = ( x, "Beginning of log")

  def example3( x: Double ): Unit = {
    val result = bind3( outer3 )(
      bind3( inner3 )( unit3( x ) )
    )

    println( "EXAMPLE #3 - Adding a plumbing function" )
    println( s"Input:  $x" )
    println( s"Result: ${result._1}" )
    println( s"Log:\n  ${result._2}" )
    println( "" )
  }

  /**
   * EXAMPLE #4
   * Renaming and cleaning: use better names, use currying and explicit the
   * output data structures so that it becomes more practical to use the
   * binding function.
   */
  type Debuggable4 = ( Double, String )

  def outer4( x: Double ): Debuggable4 = ( Math.log(x), s"Called outer4($x)" )

  def inner4( x: Double ): Debuggable4 = ( Math.exp(x), s"Called inner4($x)" )

  def bind4( f: Double => Debuggable4 )( x: Debuggable4 ): Debuggable4 = {
    f( x._1 ) match {
      case ( fx, msg ) => ( fx, x._2 + "\n  " + msg )
    }
  }

  def unit4( x: Double, log: String ): Debuggable4 = ( x, log )

  def example4( x: Double ): Unit = {
    val initial = unit4( x, "Beginning of logs" )
    val outer = bind4( outer4 )( _ )
    val inner = bind4( inner4 )( _ )
    val result = outer( inner( initial ) )

    println( "EXAMPLE #4 - Renaming and cleaning" )
    println( s"Input:  $x" )
    println( s"Result: ${result._1}" )
    println( s"Log:\n  ${result._2}" )
    println( "" )
  }

  /**
   * EXAMPLE #5
   * Explicit Debuggable monad: create a class that incorporates the generic
   * functionalities developed in the previous example and that makes the
   * usage of these functionalities even more practical.
   */
  case class Debuggable5 private ( x: Double, log: String ) {
    def bind( f: Double => Debuggable5 ): Debuggable5 = {
      f( x ) match {
        case Debuggable5( fx, msg ) => Debuggable5( fx, log + "\n  " + msg )
      }
    }
  }
  object Debuggable5 {
    def apply( x: Double, log: String ): Debuggable5 = new Debuggable5( x, log )
  }

  def outer5( x: Double ) = Debuggable5( Math.log(x), s"Called outer5($x)" )

  def inner5( x: Double ) = Debuggable5( Math.exp(x), s"Called inner5($x)" )

  def example5( x: Double ): Unit = {
    val result = Debuggable5( x, "Beginning of log" )
      .bind( inner5 )
      .bind( outer5 )

    println( "EXAMPLE #5 - Explicit Debuggable monad" )
    println( s"Input:  $x" )
    println( s"Result: ${result.x}" )
    println( s"Log:\n  ${result.log}" )
    println( "" )
  }

  /**
   * EXAMPLE #6
   * Scala idiomatic way of monads: Scala allows to use a special syntax with
   * a for-expression to work with monads in a nicer way. And usability is
   * improved even more.
   */
  case class Debuggable6( x: Double, log: String ) {
    def flatMap( f: Double => Debuggable6 ): Debuggable6 = f( x ) match {
      case Debuggable6( fx, msg ) => Debuggable6( fx, log + "\n  " + msg )
    }
    def map( f: Double => Double ): Debuggable6 = Debuggable6( f( x ), log )
  }

  def outer6( x: Double ) = Debuggable6( Math.log(x), s"Called outer6($x)" )

  def inner6( x: Double ) = Debuggable6( Math.exp(x), s"Called inner6($x)" )

  def example6( x: Double ): Unit = {
    val result = for {
      y1 <- Debuggable6( x, "Beginning of log" )
      y2 <- inner6( y1 )
      y3 <- outer6( y2 )
    } yield y3

    println( "EXAMPLE #6 - Scala idiomatic monads" )
    println( s"Input:  $x" )
    println( s"Result: ${result.x}" )
    println( s"Log:\n  ${result.log}" )
    println( "" )
  }

  /**
    * EXAMPLE #7
    * Introduce generics: use generic types for the Debuggable monad so that it can
    * be used in different contexts and with more than just logs
    */
  case class Writer[+A, B]( x: A, ys: Traversable[B] ) {
    def flatMap[T]( f: A => Writer[T, B] ): Writer[T, B] = f( x ) match {
      case Writer( fx, y ) => Writer[T, B]( fx, ys ++: y )
    }
    def map[T]( f: A => T ): Writer[T, B] = Writer[T, B]( f( x ), ys )
  }

  def outer7( x: Double ) = Writer( Math.log(x), Seq(s"Called outer7($x)") )

  def inner7( x: Double ) = Writer( Math.exp(x), Seq(s"Called inner7($x)") )

  def example7( x: Double ): Unit = {
    val result = for {
      y1 <- Writer( x, Seq("Beginning of log") )
      y2 <- inner7( y1 )
      y3 <- outer7( y2 )
    } yield y3

    println( "EXAMPLE #7 - Introduce generics" )
    println( s"Input:  $x" )
    println( s"Result: ${result.x}" )
    println( s"Log:\n  ${result.ys.mkString("\n  ")}" )
    println( "" )
  }
}