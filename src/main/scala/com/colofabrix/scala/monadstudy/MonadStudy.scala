package com.colofabrix.scala.monadstudy

import com.colofabrix.scala.monadstudy.Multivalued.Complex

/**
  * Some practice with the topics explained in the article
  * http://blog.sigfpe.com/2006/08/you-could-have-invented-monads-and.html
  */

object MonadStudy {

  def main(args: Array[String]): Unit = {
    val number = new java.util.Random().nextDouble()
    println(s"\nNUMBER: $number\n")

    println( "\n ~ Debuggable ~ \n" )

    Debuggable.example1(number)
    Debuggable.example2(number)
    Debuggable.example3(number)
    Debuggable.example4(number)
    Debuggable.example5(number)
    Debuggable.example6(number)
    Debuggable.example7(number)

    println( "\n ~ Multivalued ~ \n" )

    Multivalued.example1(Complex(4, 0))
    Multivalued.example2(Complex(4, 0))
    //Multivalued.example3(number)
    //Multivalued.example4(number)
    //Multivalued.example5(number)
    //Multivalued.example6(number)
    //Multivalued.example7(number)
  }

}
