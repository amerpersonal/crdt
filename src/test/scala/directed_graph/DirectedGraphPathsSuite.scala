package directed_graph

import data.Vertex
import graphs.directed_graph.DirectedGraph
import org.scalatest.funsuite.AnyFunSuite

class DirectedGraphPathsSuite extends AnyFunSuite {

  test("paths empty for empty graph") {
    val g = DirectedGraph[Int]()

    assert(g.findPaths(Vertex(0), Vertex(1)) == Vector.empty)
  }

  test("all paths between nodes found correctly in 3 nodes graphs.graph") {
    val g: DirectedGraph[Int] = DirectedGraph[Int](0, 1, 2)

    g.addEdge(Vertex(0), Vertex(1))
    g.addEdge(Vertex(0), Vertex(2))
    g.addEdge(Vertex(1), Vertex(2))
    g.addEdge(Vertex(2), Vertex(0))

    val paths = g.findPaths(Vertex(0), Vertex(2))

    assert(paths.sortBy(_.size) == Vector(Vector(Vertex(0), Vertex(2)), Vector(Vertex(0), Vertex(1), Vertex(2))))
  }

  test("all paths between nodes found correctly in 5 nodes char graphs.graph") {
    val g: DirectedGraph[Int] = DirectedGraph(0, 1, 2, 3)

    g.addEdge(Vertex(0), Vertex(1))
    g.addEdge(Vertex(0), Vertex(2))
    g.addEdge(Vertex(1), Vertex(2))
    g.addEdge(Vertex(2), Vertex(0))
    g.addEdge(Vertex(2), Vertex(3))
    g.addEdge(Vertex(3), Vertex(3))

    val paths = g.findPaths(Vertex(0), Vertex(3))

    assert(paths.sortBy(_.size) == Vector(Vector(Vertex(0), Vertex(2), Vertex(3)), Vector(Vertex(0), Vertex(1), Vertex(2), Vertex(3))))
  }
}
