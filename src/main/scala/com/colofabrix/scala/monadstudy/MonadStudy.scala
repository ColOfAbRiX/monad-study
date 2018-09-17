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
    Debuggable.example5(number)
    Debuggable.example6(number)
    Debuggable.example7(number)
  }

}
