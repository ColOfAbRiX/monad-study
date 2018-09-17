package com.colofabrix.scala.monadstudy

object Debuggable {

  /**
   * EXAMPLE #1
   * Normal functions composition: calling one function and passing its
   * result to another one.
   */
  def outer1( x: Double ): Double = Math.log( x )
  def inner1( x: Double ): Double = Math.exp( x )
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
  def outer2( x: Double ): ( Double, String ) = ( Math.log( x ), s"Called outer2($x)" )

  def inner2( x: Double ): ( Double, String ) = ( Math.exp( x ), s"Called inner2($x)" )

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
   * Adding a plumbing function: I don't want the caller to manage it, the
   * caller should only use it, so I create a new plumbing function that
   * does the work for me.
   */
  def outer3( x: Double ): ( Double, String ) = ( Math.log( x ), s"Called outer3($x)" )

  def inner3( x: Double ): ( Double, String ) = ( Math.exp( x ), s"Called inner3($x)" )

  def bind3( f: Double => ( Double, String ) )( x: ( Double, String ) ): ( Double, String ) = {
    f( x._1 ) match {
      case ( fx, msg ) => ( fx, x._2 + "\n  " + msg )
    }
  }

  def unit3( x: Double ): ( Double, String ) = ( x, "  Beginning of log")

  def example3( x: Double ): Unit = {
    val result = bind3( outer3 )(
      bind3( inner3 )( unit3( x ) )
    )

    println( "EXAMPLE #3 - Adding a plumbing function" )
    println( s"Input:  $x" )
    println( s"Result: ${result._1}" )
    println( s"Log: \n${result._2}" )
    println( "" )
  }

  /**
   * EXAMPLE #4
   * Renaming and cleaning: use better names, use currying and explicit the
   * output data structures.
   */
  type Writer4 = ( Double, String )

  def outer4( x: Double ): Writer4 = ( Math.log( x ), s"Called outer4($x)" )

  def inner4( x: Double ): Writer4 = ( Math.exp( x ), s"Called inner4($x)" )

  def bind4( f: Double => Writer4 )( x: Writer4 ): Writer4 = {
    f( x._1 ) match {
      case ( fx, msg ) => ( fx, x._2 + "\n  " + msg )
    }
  }

  def unit4( x: Double ): Writer4 = ( x, "  Beginning of log")

  def example4( x: Double ): Unit = {
    val outer = bind4( outer4 )( _ )
    val inner = bind4( inner4 )( _ )
    val result = outer( inner( unit4( x ) ) )

    println( "EXAMPLE #4 - Renaming and cleaning" )
    println( s"Input:  $x" )
    println( s"Result: ${result._1}" )
    println( s"Log: \n${result._2}" )
    println( "" )
  }

  /**
   * EXAMPLE #5
   * Explicit Writer monad: create a class that incorporate the generic
   * functionalities developed in the previous example and that makes its
   * usage more comfortable.
   */
  case class Writer5 private ( x: Double, log: String ) {
    def bind( f: Double => Writer5 ): Writer5 = {
      f( x ) match {
        case Writer5( fx, msg ) => Writer5( fx, log + "\n  " + msg )
      }
    }
  }
  object Writer5 {
    def apply( x: Double, log: String = "  Beginning of log" ): Writer5 = new Writer5( x, log )
  }

  def outer5( x: Double ) = Writer5( Math.log( x ), s"Called outer5($x)" )

  def inner5( x: Double ) = Writer5( Math.exp( x ), s"Called inner5($x)" )

  def example5( x: Double ): Unit = {
    val result = Writer5( x )
      .bind( inner5 )
      .bind( outer5 )

    println( "EXAMPLE #5 - Explicit Writer monad" )
    println( s"Input:  $x" )
    println( s"Result: ${result.x}" )
    println( s"Log: \n${result.log}" )
    println( "" )
  }

  /**
   * EXAMPLE #6
   * Scala idiomatic way of monads: Scala allows to use a special syntax with
   * a for-expression to work with monads in a nicer way.
   */
  case class Writer6( x: Double, log: String = "  Beginning of log" ) {
    def flatMap( f: Double => Writer6 ): Writer6 = f( x ) match {
      case Writer6( fx, msg ) => Writer6( fx, log + "\n  " + msg )
    }
    def map( f: Double => Double ): Writer6 = Writer6( f( x ), log )
  }

  def outer6( x: Double ) = Writer6( Math.log( x ), s"Called outer6($x)" )

  def inner6( x: Double ) = Writer6( Math.exp( x ), s"Called inner6($x)" )

  def example6( x: Double ): Unit = {
    val result = for {
      y1 <- Writer6( x )
      y2 <- inner6( y1 )
      y3 <- outer6( y2 )
    } yield y3

    println( "EXAMPLE #6 - Scala idiomatic monads" )
    println( s"Input:  $x" )
    println( s"Result: ${result.x}" )
    println( s"Log: \n${result.log}" )
    println( "" )
  }
}