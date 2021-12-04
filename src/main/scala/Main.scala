
import scala.collection.mutable

object Main {



  case class Vertex[A](value: A)

  case class TimestampedVertex[A](timestamp: Long, vertex: Vertex[A])

  case class Graph(nodes: Set[Int], adjacency: mutable.Map[Int, Set[Int]]){

    def size = nodes.size

    def addVertex(vertex: Int) = {
      adjacency.update(vertex, Set.empty[Int])
    }

    def removeVertex(vertex: Int) = {
      adjacency.remove(vertex)
    }

    def addEdge(v1: Int, v2: Int) = {
      val v1adj: Set[Int] = adjacency.getOrElse(v1, Set.empty[Int])
      adjacency.update(v1, adjacency.getOrElse(v1, Set.empty[Int]) + v2)
      //adjacency.update(v2, adjacency.getOrElse(v1, Set.empty[Int]) + v1)
    }

    def removeEdge(v1: Int, v2: Int) = {
      adjacency.update(v1, adjacency.getOrElse(v1, Set.empty[Int]) - v2)
      //adjacency.update(v2, adjacency.getOrElse(v1, Set.empty[Int]) - v1)
    }

    def isInGraph(v: Int) = nodes.contains(v)

    def getEdges(v: Int) = adjacency.getOrElse(v, Set.empty)

    def findPaths(start: Int, end: Int): Vector[Vector[Int]] = {
      def loop(current: Int, path: Vector[Int]): Vector[Vector[Int]]  = {
        if (current == end) Vector(path :+ current)
        else {
          val adj: Vector[Int] = path.lastOption.map { l =>
            adjacency.getOrElse(current, Set.empty).toVector.filter(_ != l)
          }.getOrElse(adjacency.getOrElse(current, Set.empty).toVector)

          if (adj.isEmpty) Vector()
          else adj.flatMap { el =>
            loop(el, path :+ current)
          }
        }
      }

      loop(start, Vector.empty[Int])
    }

//    def dsf() {
//      def loop(graph: Graph, node: Int, paths: List[List[Int]], visited: List[Int], stack: List[Int]): List[Int] = {
//        if (stack.isEmpty || visited.lastOption == Some(node)) visited
//        else {
//          val next = stack.head
//
//          val stackNew = graph.getEdges(node).toList
//
//          if (visited.contains(next)) dfsLoop(graph, node, visited, stackNew)
//          else dfsLoop(graph, node, next :: visited, stackNew)
//        }
//      }
//    }
  }



  object Graph {

    def apply(elements: Int*): Graph = {
      val adjancency = mutable.Map(elements.map(_ -> Set.empty[Int]).toSeq: _*)

      Graph(elements.toSet, adjancency)
    }
  }

  def main(args: Array[String]): Unit = {
    println("rema")

    val g: Graph = Graph(0, 1, 2)

    g.addEdge(0, 1)
    g.addEdge(0, 2)
    g.addEdge(2, 0)
    g.addEdge(1, 2)

    val paths = g.findPaths(0, 2)

    println("PATHS")
    paths.foreach { path =>
      println(path.mkString(" - "))
    }
  }
}
