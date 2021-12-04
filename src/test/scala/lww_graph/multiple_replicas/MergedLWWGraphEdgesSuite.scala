package lww_graph.multiple_replicas

import data.Vertex
import lww_graph.LWWGraph
import org.scalatest.funsuite.AnyFunSuite

class MergedLWWGraphEdgesSuite extends AnyFunSuite {
  test("merge empty graph and graph with one edge") {
    val (r1, r2) = (LWWGraph[Int](1, 2), LWWGraph[Int]())

    r1.addEdge(Vertex(1), Vertex(2))

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.getEdges(Vertex(1)) == Set(Vertex(2)))
  }

  test("merging two replicas with only added edges") {
    val (r1, r2) = (LWWGraph[Int](1, 2, 3), LWWGraph[Int](1, 2, 3))

    r1.addEdge(Vertex(1), Vertex(2))
    Thread.sleep(1)
    r2.addEdge(Vertex(1), Vertex(3))

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.getEdges(Vertex(1)) == Set(Vertex(2), Vertex(3)))
  }

  test("remove edge on different replica") {
    val (r1, r2) = (LWWGraph[Int](1, 2), LWWGraph[Int](1, 2))

    r1.addEdge(Vertex(1), Vertex(2))

    Thread.sleep(1)
    r2.removeEdge(Vertex(1), Vertex(2))

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.getEdges(Vertex(1)) == Set.empty)
  }

  test("add edge after remove on different replica") {
    val (r1, r2) = (LWWGraph[Int](1, 2), LWWGraph[Int](1, 2))

    r1.addEdge(Vertex(1), Vertex(2))
    Thread.sleep(1)
    r2.removeEdge(Vertex(1), Vertex(2))
    Thread.sleep(1)
    r1.removeEdge(Vertex(1), Vertex(2))
    Thread.sleep(1)
    r2.addEdge(Vertex(1), Vertex(2))

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.getEdges(Vertex(1)) == Set((Vertex(2))))
  }

  test("remove node with edges from different replica removes the edge") {
    val (r1, r2) = (LWWGraph[Int](1, 2), LWWGraph[Int](1, 2))


    r1.addEdge(Vertex(1), Vertex(2))

    Thread.sleep(1)

    r2.removeVertex(Vertex(1))

    val merged = LWWGraph.merge(r1, r2)
    assert(merged.getEdges(Vertex(1)) == Set.empty)
  }

  test("add edge to node that is added on different replica") {
    val (r1, r2) = (LWWGraph[Int](1, 2), LWWGraph[Int](1, 2))

    val source = Vertex(2)
    val destination = Vertex(3)

    r2.addVertex(destination)

    Thread.sleep(1)

    r1.addEdge(source, destination)

    val merged = LWWGraph.merge(r1, r2)
    assert(merged.getEdges(source) == Set(destination))
  }

  test("add edge from node that is added on different replica") {
    val (r1, r2) = (LWWGraph[Int](1, 2), LWWGraph[Int](1, 2))

    val source = Vertex(3)
    val destination = Vertex(2)
    r2.addVertex(source)

    Thread.sleep(1)

    r1.addEdge(source, destination)

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.getEdges(source) == Set(destination))
  }
}
