package com.colofabrix.scala.monadstudy

object Debuggable {

  /**
   * Normal functions composition: calling one function and passing its
   * result to another one.
    */
  def outer1( x: Double ): Double = 2.0 * Math.pow( x, 2.0 ) - 5.0
  def inner1( x: Double ): Double = -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0
  def example1( x: Double ): Unit = {
    val y = outer1( inner1( x ) )

    println( "EXAMPLE #1 - Normal functions composition" )
    println( s"Result: $y" )
    println( "" )
  }

  /**
   * Adding some FP-style debugging: I can't have any side effect so I must
   * return the log as output parameter of the function. The caller will
   * manage this return value.
    */
  def outer2( x: Double ): (Double, String) = Tuple2(
    2.0 * Math.pow( x, 2.0 ) - 5.0,
    s"Called outer2($x)"
  )

  def inner2( x: Double ): (Double, String) = Tuple2(
    -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
    s"Called inner2($x)"
  )

  def example2( x: Double ): Unit = {
    val (y1, msg1) = inner2( x )
    val (y, msg2) = outer2( y1 )
    val msg = msg1 + "\n  " + msg2

    println( "EXAMPLE #2 - Adding some FP-style debugging" )
    println( s"Result: $y" )
    println( s"Log:\n  $msg" )
    println( "" )
  }

  /**
   * Adding a plumbing function: I don't want the caller to manage it, the
   * caller should only use it, so I create a new plumbing function that
   * does the work for me.
    */
  def outer3( x: Double ): (Double, String) = Tuple2(
    2.0 * Math.pow( x, 2.0 ) - 5.0,
    s"Called outer3($x)"
  )

  def inner3( x: Double ): (Double, String) = Tuple2(
    -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
    s"Called inner3($x)"
  )

  def bind3( x: (Double, String) )( f: Double => (Double, String) ): (Double, String) = {
    f( x._1 ) match {
      case ( fx, msg ) => (fx, x._2 + "\n  " + msg)
    }
  }

  def unit3( x: Double ): (Double, String) = (x, "  Beginning of log")

  def example3( n: Double ): Unit = {
    val result = bind3(
      bind3( unit3( n ) )( inner3 )
    )( outer3 )

    println( "EXAMPLE #3 - Adding a plumbing function" )
    println( s"Result: ${result._1}" )
    println( s"Log: \n${result._2}" )
    println( "" )
  }

  /**
    * Renaming and cleaning: use better names and explicit the output data
    * structure.
    */
  type Writer4 = (Double, String)

  def outer4( x: Double ): Writer4 = Tuple2(
    2.0 * Math.pow( x, 2.0 ) - 5.0,
    s"Called outer4($x)"
  )

  def inner4( x: Double ): Writer4 = Tuple2(
    -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
    s"Called inner4($x)"
  )

  def bind4( x: Writer4 )( f: Double => Writer4 ): Writer4 = {
    f( x._1 ) match {
      case ( fx, msg ) => (fx, x._2 + "\n  " + msg)
    }
  }

  def unit4( x: Double ): Writer4 = (x, "  Beginning of log")

  def example4( n: Double ): Unit = {
    val result = bind4(
      bind4( unit4( n ) )( inner4 )
    )( outer4 )

    println( "EXAMPLE #4 - Renaming and cleaning" )
    println( s"Result: ${result._1}" )
    println( s"Log: \n${result._2}" )
    println( "" )
  }

  /**
   * Explicit Writer monad: create a class that incorporate the generic
   * functionalities developed in the previous example and that makes its
   * usage more comfortable.
    */
  case class Writer5(x: Double, log: String = "") {
    def bind( f: Double => Writer5 ): Writer5 = {
      f( x ) match {
        case Writer5( fx, msg ) => Writer5(fx, log + "\n  " + msg)
      }
    }
  }

  def outer5( x: Double ) = Writer5(
    2.0 * Math.pow( x, 2.0 ) - 5.0,
    s"Called outer5($x)"
  )

  def inner5( x: Double ) = Writer5(
    -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
    s"Called inner5($x)"
  )

  def example5( n: Double ): Unit = {
    val result = Writer5( n )
      .bind( inner5 )
      .bind( outer5 )

    println( "EXAMPLE #5 - Explicit Writer monad" )
    println( s"Result: ${result.x}" )
    println( s"Log: \n${result.log}" )
    println( "" )
  }

  /**
    * Scala idiomatic way of monads
    */
  case class Writer6(x: Double, log: String = "") {
    def outerp( f: Double => Writer6 ): Writer6 = f( x ) match {
      case Writer6( fx, msg ) => Writer6(fx, log + "\n  " + msg)
    }
    def map( f: Double => Double ) = f( x )
  }

  def outer6( x: Double ) = Writer6(
    2.0 * Math.pow( x, 2.0 ) - 6.0,
    s"Called outer6($x)"
  )

  def inner6( x: Double ) = Writer6(
    -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
    s"Called inner6($x)"
  )

  // def example6( n: Double ): Unit = {
  //   val result = for {
  //     n <- Writer6(4)
  //     y1 <- inner6(n)
  //     y2 <- outer6(y1)
  //   } yield y2

  //   println( "EXAMPLE #6 - Scala idiomatic way of monads" )
  //   println( s"Result: ${result.x}" )
  //   println( s"Log: \n${result.log}" )
  //   println( "" )
  // }

}