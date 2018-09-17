package com.colofabrix.scala.monadstudy

/**
  * Some practice with the topics explained in the article
  * http://blog.sigfpe.com/2006/08/you-could-have-invented-monads-and.html
  */

object MonadStudy {

  case class Complex( r: Double, t: Double ) {
    private def deMoivreRoots( root: Int ) = Seq.tabulate( root ) { n =>
      Complex( Math.sqrt( r ) * Math.cos( (t + n * 2 * Math.PI) / root ), Math.sqrt( r ) * Math.sin( (t + n * 2 * Math.PI) / root ) )
    }

    def sqrt(): Seq[Complex] = deMoivreRoots( 2 )

    def cbrt(): Seq[Complex] = deMoivreRoots( 3 )
  }

  def main(args: Array[String]): Unit = {
    val number = new java.util.Random().nextDouble()
    println(s"\nNUMBER: $number\n")

    Debuggable.example1(number)
    Debuggable.example2(number)
    Debuggable.example3(number)
    Debuggable.example4(number)
    Debuggable.example5(number)
    Debuggable.example6(number)
    Debuggable.example7(number)
  }

}
