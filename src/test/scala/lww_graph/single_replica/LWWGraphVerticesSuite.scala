package lww_graph.single_replica

import data.Vertex
import lww_graph.LWWGraph
import org.scalatest.funsuite.AnyFunSuite

class LWWGraphVerticesSuite extends AnyFunSuite {
  test("empty graph does not contain any vertices") {
    val g = LWWGraph[Int]()

    assert(g.contains(Vertex(0)) == false)
  }

  test("graph should contain singly added vertex") {
    val g = LWWGraph[Int]()

    g.addVertex(Vertex(0))

    assert(g.contains(Vertex(0)) == true)
  }

  test("graph should not contain vertex that is removed again after added") {
    val g = LWWGraph[Int]()

    g.addVertex(Vertex(0))
    g.removeVertex(Vertex(0))

    assert(g.contains(Vertex(0)) == false)
  }

  test("graph should contain vertex that is added again after removal") {
    val g = LWWGraph[Int]()

    g.addVertex(Vertex(0))
    g.removeVertex(Vertex(0))
    g.addVertex(Vertex(0))

    assert(g.contains(Vertex(0)) == true)
  }
}
