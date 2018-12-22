package com.colofabrix.scala.transformers

import com.colofabrix.scala.monads._
import com.colofabrix.scala.monads.List._

object Examples {

  def run(): Unit = {
    println(
      """
        |  ~ ~  MONAD TRANFORMERS STUDY  ~ ~
        """.stripMargin
    )

    println( "\n ~ Option Transformer ~ \n" )

    // Without transformers it's ugly!
    val myListOption: List[Option[String]] = Some("a") :: Some("s") :: None :: Some("f") :: Nil
    val result = for {
      x <- myListOption
    } yield {
      for {
        y <- x
      } yield {
        y.toUpperCase()
      }
    }
    println(s"\nWithout transformer:\n  $result")

    // Let's try with the OptionT monad transformer
    val result2 = (for {
      x <- OptionT( myListOption )
    } yield {
      x.toUpperCase()
    }).value
    println(s"\nUsing OptionT:\n  $result")
  }
}
