package com.colofabrix.scala.transformers

import com.colofabrix.scala.monads.List._
import com.colofabrix.scala.monads.Option._
import com.colofabrix.scala.monads._

object Examples {

  def run(): Unit = {
    println(
      """
        |  ~ ~  MONAD TRANSFORMERS STUDY  ~ ~
        """.stripMargin
    )

    println( "\n ~ OptionT Transformer ~ \n" )

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
    println(s"Without transformer:\n  $result")

    // Let's try with the OptionT monad transformer
    val result2 = (for {
      x <- OptionT( myListOption )
    } yield {
      x.toUpperCase()
    }).value
    println(s"\nUsing OptionT:\n  $result2")


    println( "\n ~ ListT Transformer ~ \n" )

    val myOptionList: Option[List[String]] = Some("a" :: "s" :: "d" :: "f" :: Nil)
    val result3 = for {
      x <- myOptionList
    } yield {
      for {
        y <- x
      } yield {
        y.toUpperCase()
      }
    }
    println(s"Without transformer:\n  $result3")

    val result4 = (for {
      x <- ListT( myOptionList )
    } yield {
      x.toUpperCase()
    }).value
    println(s"\nUsing ListT:\n  $result4")

    println( "\n ~ More complex example ~ \n" )

    // Fakes to find all the users that contains a specific name
    def findUsersByName( name: String ): ListT[Option, String] = ListT(
      if( name.length >= 5 ) {
        Some(Seq.tabulate(name.length - 5)(i => s"${name}_$i").toList)
      } else {
        None
      }
    )

    // Fakes to find the companies associated with a user
    def findCompanyByUser( user: String ): ListT[Option, String] = ListT(
      Some(s"${user}_company" :: Nil)
    )

    // Using the above function to watch transformers in action
    val result5 = (for {
      user    <- findUsersByName("fabrizio")
      company <- findCompanyByUser(user)
    } yield company).value
    println(s"Result: $result5")

  }
}
