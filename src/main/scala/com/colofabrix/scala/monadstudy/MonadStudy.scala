package com.colofabrix.scala.monadstudy

/**
  * Some practice with the topics explained in the article
  * http://blog.sigfpe.com/2006/08/you-could-have-invented-monads-and.html
  */

object MonadStudy {

  def main(args: Array[String]): Unit = {
    val number = new java.util.Random().nextDouble()
    println(s"\nNUMBER: $number\n")
    Debuggable.example1(number)
    Debuggable.example2(number)
    Debuggable.example3(number)
    Debuggable.example4(number)
  }

  object Debuggable {

    /**
      * Normal functions
      */
    def f1( x: Double ): Double = 2.0 * Math.pow( x, 2.0 ) - 5.0
    def g1( x: Double ): Double = -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0
    def example1( x: Double ): Unit = {
      val y = f1( g1( x ) )

      println( "EXAMPLE #1" )
      println( s"Result: $y" )
      println( "" )
    }

    /**
      * Adding some FP-style debugging
      */
    def fLog2( x: Double ): (Double, String) = Tuple2(
      2.0 * Math.pow( x, 2.0 ) - 5.0,
      s"Called f2($x)"
    )

    def gLog2( x: Double ): (Double, String) = Tuple2(
      -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
      s"Called g2($x)"
    )

    def example2( x: Double ): Unit = {
      val (y1, msg1) = gLog2( x )
      val (y, msg2) = fLog2( y1 )
      val msg = msg1 + "\n  " + msg2

      println( "EXAMPLE #2" )
      println( s"Result: $y" )
      println( s"Log:\n  $msg" )
      println( "" )
    }

    /**
      * Adding a bind function
      */
    def fLog3( x: Double ): (Double, String) = Tuple2(
      2.0 * Math.pow( x, 2.0 ) - 5.0,
      s"Called f2($x)"
    )

    def gLog3( x: Double ): (Double, String) = Tuple2(
      -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
      s"Called g2($x)"
    )

    def bind3( x: (Double, String) )( f: Double => (Double, String) ): (Double, String) = {
      f( x._1 ) match {
        case (fx, msg) => (fx, x._2 + "\n  " + msg)
      }
    }

    def unit3( x: Double ): (Double, String) = (x, "  Beginning of log")

    def example3( n: Double ): Unit = {
      val result = bind3(
        bind3( unit3( n ) )( gLog3 )
      )( fLog3 )

      println( "EXAMPLE #3" )
      println( s"Result: ${result._1}" )
      println( s"Log: \n${result._2}" )
      println( "" )
    }

    /**
      * Renaming and cleanings
      */
    type Writer4 = (Double, String)

    def fLog4( x: Double ): Writer4 = Tuple2(
      2.0 * Math.pow( x, 2.0 ) - 5.0,
      s"Called f2($x)"
    )

    def gLog4( x: Double ): Writer4 = Tuple2(
      -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
      s"Called g2($x)"
    )

    def bind4( x: Writer4 )( f: Double => Writer4 ): Writer4 = {
      f( x._1 ) match {
        case (fx, msg) => (fx, x._2 + "\n  " + msg)
      }
    }

    def unit4( x: Double ): Writer4 = (x, "  Beginning of log")

    def example4( n: Double ): Unit = {
      val result = bind4(
        bind4( unit4( n ) )( gLog4 )
      )( fLog4 )

      println( "EXAMPLE #4" )
      println( s"Result: ${result._1}" )
      println( s"Log: \n${result._2}" )
      println( "" )
    }

    /**
      * Complete Writer monad
      */
    case class Writer5(x: Double, log: String = "") {
      def bind( f: Double => Writer5 ): Writer5 = {
        f( x ) match {
          case Writer5(fx, msg) => Writer5(fx, log + "\n  " + msg)
        }
      }
    }

    def fLog5( x: Double ) = Writer5(
      2.0 * Math.pow( x, 2.0 ) - 5.0,
      s"Called f2($x)"
    )

    def gLog5( x: Double ) = Writer5(
      -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
      s"Called g2($x)"
    )

    def example5( n: Double ): Unit = {
      val result = Writer5(n).bind(fLog5).bind(gLog5)

      println( "EXAMPLE #5" )
      println( s"Result: ${result.x}" )
      println( s"Log: \n${result.log}" )
      println( "" )
    }

    /**
      * Scala idiomatic Writer monad
      */
    case class Writer6(x: Double, log: String = "") {
      def flatMap( f: Double => Writer6 ): Writer6 = f( x ) match {
        case Writer6(fx, msg) => Writer6(fx, log + "\n  " + msg)
      }
      def map( f: Double => Double ) = f( x )
    }

    def fLog6( x: Double ) = Writer6(
      2.0 * Math.pow( x, 2.0 ) - 6.0,
      s"Called f2($x)"
    )

    def gLog6( x: Double ) = Writer6(
      -Math.pow( x, 3.0 ) - 2.0 * Math.pow( x, 2.0 ) + 4.0,
      s"Called g2($x)"
    )

    def example6( n: Double ): Unit = {
      val result = for {
        n <- Writer6(4)
        y1 <- gLog6(n)
        y2 <- fLog6(y1)
      } yield y2

      println( "EXAMPLE #6" )
      println( s"Result: ${result.x}" )
      println( s"Log: \n${result.log}" )
      println( "" )
    }

  }
}
