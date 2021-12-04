package directed_graph

import data.Vertex
import graphs.directed_graph.DirectedGraph
import org.scalatest.funsuite.AnyFunSuite

class DirectedGraphVerticesSuite extends AnyFunSuite {
  test("empty graphs.graph has empty size") {
    val g = DirectedGraph()

    assert(g.size == 0)
  }

  test("contains on empty graphs.graph returns false") {
    val g = DirectedGraph[Int]()

    assert(g.contains(Vertex(5)) == false)
  }


  test("contains works correctly before any edges added") {
    val g = DirectedGraph[Int](2, 6, 9, 11)

    assert(g.contains(Vertex(5)) == false)
    assert(g.contains(Vertex(6)) == true)
    assert(g.contains(Vertex(2)) == true)
    assert(g.contains(Vertex(10)) == false)
  }


  test("add non existing vertex returns true") {
    val g = DirectedGraph[Int]()

    assert(g.addVertex(Vertex(2)))
  }

  test("add existing vertex returns false") {
    val g = DirectedGraph[Int](2)

    assert(g.addVertex(Vertex(2)) == false)
  }


  test("remove non existing vertex returns false") {
    val g = DirectedGraph[Int]()

    assert(g.removeVertex(Vertex(2)) == false)
  }

  test("remove existing vertex returns true") {
    val g = DirectedGraph[Int](2)

    assert(g.removeVertex(Vertex(2)) == true)
  }

  test("add non existing vertex works as expected") {
    val g = DirectedGraph[Int]()

    g.addVertex(Vertex(2))

    assert(g.contains(Vertex(2)))
  }

  test("remove existing vertex works as expected") {
    val g = DirectedGraph[Int](2)

    g.removeVertex(Vertex(2))

    assert(g.contains(Vertex(2)) == false)
  }

}

