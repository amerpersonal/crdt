
import data.Vertex
import graph.Graph

import scala.collection.mutable

object Main {


  def main(args: Array[String]): Unit = {
    println("rema")

    val g: Graph[Int] = Graph[Int](0, 1, 2, 3)

    g.addEdge(Vertex(0), Vertex(1))
    g.addEdge(Vertex(0), Vertex(2))
    g.addEdge(Vertex(1), Vertex(2))
    g.addEdge(Vertex(2), Vertex(0))

    g.addEdge(Vertex(2), Vertex(3))


    g.addEdge(Vertex(3), Vertex(3))

    val paths = g.findPaths(Vertex(0), Vertex(3))

    println("PATHS")
    paths.foreach { path =>
      println(path.mkString(" - "))
    }
  }
}
