# Studying Monads in Scala

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

## Author Information

Fabrizio Colonna (@ColOfAbRiX)
