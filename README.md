# Studying Monads in Scala

## How to use these examples

These examples have been developed throughout my learning process towards full
functional programming and their size grew little by little.

The logical order to follow is by starting at the main module `MainMonadStudy`
and from there follow the each example, top to bottom. This will lead you to
discover the data structures I created, what problems I faced and how I solved
them.

Names on the examples might not be the familiar ones and might differ from the
standard Scala ones. I've chosen this approach on purpose to put myself in a
situation where I really had to think what is happening, without the prior
knowledge of Scala and possibly of Haskell. But sometimes it really becomes
confusing!

## You could have invented monads

Some practice with the topics explained in the article
[You Could Have Invented Monads!](1).

These exercises are aimed at understanding how monads got developed and the
concepts that lie underneath them.
  
Following the page, here are developed 3 monads:

* Writer
* List
* State 

Each exercise is structured such that monads arise as a generalization of a
common problem.

First a problem is presented in the form of "I have two functions to wire
together with some code" and then, via a series of steps, the exercise tries to
solve the problem via auxiliary function, generalization and abstraction until
we reach a very generic solution: a monad. 

[1]: http://blog.sigfpe.com/2006/08/you-could-have-invented-monads-and.html

## Generalization of objects

Taking the exercise one step further, I wanted to have a general structure to
define functional objects (monoids, functors, monads, ...) so I started working
with extension methods, type classes and higher kinded types.

The result can be seen inside the monads package where I developed several
extension methods for some common Scala objects like String, Option, List and
Function1.

After that I wanted to develop monads that are not in the standard Scala library
and I picked Writer as it's simple to understand. For Writer I had to use
partially applied types and type lambdas .

## Comonads

Carrying on with functional programming objects, the next topic are comonads. I
start with the generic definition then the basic Identity comonad to practice 
more the basic techniques of extension used in the step above.

There is a simple implementation of a Non-Empty-List comonad that shows the
meaning of the comonad methors: extract, duplicate and extend.

## Author Information

Fabrizio Colonna (@ColOfAbRiX)
