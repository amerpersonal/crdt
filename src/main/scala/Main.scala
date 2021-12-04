
import data.Vertex
import graphs.directed_graph.DirectedGraph
import lww_graph.LWWGraph

import scala.collection.mutable

object Main {


  def main(args: Array[String]): Unit = {
    println("rema")

//    val g = LWWGraph[Int](0, 1, 2, 3)
//
//    g.addEdge(Vertex(0), Vertex(1))
//    g.addEdge(Vertex(0), Vertex(2))
//    g.addEdge(Vertex(1), Vertex(2))
//    g.addEdge(Vertex(2), Vertex(0))
//
//    g.addEdge(Vertex(2), Vertex(3))
//
//
//    g.addEdge(Vertex(3), Vertex(3))

    val g = LWWGraph[Char]('A', 'B', 'C', 'D', 'E', 'F')


    g.addEdge(Vertex('A'), Vertex('B'))
    g.addEdge(Vertex('A'), Vertex('C'))
    g.addEdge(Vertex('A'), Vertex('E'))
    g.addEdge(Vertex('B'), Vertex('D'))
    g.addEdge(Vertex('B'), Vertex('E'))
    g.addEdge(Vertex('C'), Vertex('E'))
    g.addEdge(Vertex('D'), Vertex('C'))
    g.addEdge(Vertex('D'), Vertex('F'))
    g.addEdge(Vertex('F'), Vertex('C'))

    val paths = g.findPaths(Vertex('A'), Vertex('C'))

    println("PATHS")
    paths.foreach { path =>
      println(path.mkString(" - "))
    }
  }
}
