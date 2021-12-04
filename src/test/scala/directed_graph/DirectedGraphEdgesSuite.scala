package directed_graph

import data.Vertex
import graphs.directed_graph.DirectedGraph
import org.scalatest.funsuite.AnyFunSuite

class DirectedGraphEdgesSuite extends AnyFunSuite {
  test("remove edge from empty graphs.graph returns false") {
    val g = DirectedGraph[Int]()

    assert(g.removeEdge(Vertex(5), Vertex(2)) == false)
  }

  test("adding edges returns false if one of the nodes does not exists") {
    val g = DirectedGraph[Int](2, 6, 9, 11)

    assert(g.addEdge(Vertex(2), Vertex(5)) == false)
  }

  test("adding edges returns true if both nodes exist") {
    val g = DirectedGraph[Int](2, 6, 9, 11)

    assert(g.addEdge(Vertex(2), Vertex(6)))
  }

  test("empty graphs.graph does not contain the edge") {
    val g = DirectedGraph[Int]()

    assert(g.getEdges(Vertex(5)).size == 0)

  }

  test("adding edges works as expected") {
    val g = DirectedGraph[Int](2, 6, 9, 11)

    val source = Vertex(2)
    val destination = Vertex(6)
    g.addEdge(source, destination)

    assert(g.getEdges(source).contains(destination))
    assert(g.getEdges(destination).contains(source) == false)
  }

  test("remove edges works as expected") {
    val g = DirectedGraph[Int](2, 6, 9, 11)

    val source = Vertex(2)
    val destination = Vertex(6)
    g.addEdge(source, destination)
    g.addEdge(destination, source)

    g.removeEdge(destination, source)

    assert(g.getEdges(source).contains(destination))
    assert(g.getEdges(destination).contains(source) == false)
  }

}
