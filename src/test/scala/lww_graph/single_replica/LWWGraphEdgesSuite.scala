package lww_graph.single_replica

import data.Vertex
import lww_graph.LWWGraph
import org.scalatest.funsuite.AnyFunSuite

class LWWGraphEdgesSuite extends AnyFunSuite {
  test("empty graph should not contain any edges") {
    val g = LWWGraph[Int]()

    assert(g.getEdges(Vertex(0)) == Set.empty)
  }

  test("add edge in two nodes graph") {
    val g = LWWGraph[Int](1, 2)
    g.addEdge(Vertex(1), Vertex(2))

    assert(g.getEdges(Vertex(1)) == Set(Vertex(2)))
  }

  test("remove edge in two nodes graph") {
    val g = LWWGraph[Int](1, 2)
    g.addEdge(Vertex(1), Vertex(2))
    g.removeEdge(Vertex(1), Vertex(2))

    assert(g.getEdges(Vertex(1)) == Set.empty)
  }

  test("add edge after removal") {
    val g = LWWGraph[Int](1, 2)
    g.addEdge(Vertex(1), Vertex(2))
    g.removeEdge(Vertex(1), Vertex(2))
    g.addEdge(Vertex(1), Vertex(2))

    assert(g.getEdges(Vertex(1)) == Set(Vertex(2)))
  }
}
