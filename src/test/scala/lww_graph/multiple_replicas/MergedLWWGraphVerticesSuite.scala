package lww_graph.multiple_replicas

import data.Vertex
import lww_graph.LWWGraph
import org.scalatest.funsuite.AnyFunSuite

class MergedLWWGraphVerticesSuite extends AnyFunSuite {

  test("merging two empty replicas results in empty LWW graph") {
    val r1 = LWWGraph[Int]()
    val r2 = LWWGraph[Int]()

    assert(LWWGraph.merge(r1, r2).size == 0)
  }

  test("merging two replicas with only added vertices results in correct size of the result graph") {
    val r1 = LWWGraph[Int]()
    val r2 = LWWGraph[Int]()

    r1.addVertex(Vertex(2))
    r2.addVertex(Vertex(5))
    val merged = LWWGraph.merge(r1, r2)

    assert(merged.size == 2)
  }

  test("merge empty and single node graph results in graph that contains target node") {
    val r1 = LWWGraph[Int]()
    val r2 = LWWGraph[Int]()

    val vertex = Vertex(2)
    r1.addVertex(vertex)

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.contains(vertex))
  }

  test("merge two single node graphs results in graph that contains target nodes") {
    val r1 = LWWGraph[Int]()
    val r2 = LWWGraph[Int]()

    val vertex1 = Vertex(2)
    r1.addVertex(vertex1)

    val vertex2 = Vertex(5)
    r2.addVertex(vertex2)

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.contains(vertex1))
    assert(merged.contains(vertex2))
  }

  test("remove node after add on different replicas") {
    val r1 = LWWGraph[Int]()
    val r2 = LWWGraph[Int]()

    val vertex1 = Vertex(2)
    r1.addVertex(vertex1)

    Thread.sleep(1)
    r2.removeVertex(vertex1)

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.contains(vertex1) == false)
  }

  test("add node after remove on different replicas") {
    val r1 = LWWGraph[Int]()
    val r2 = LWWGraph[Int]()

    val vertex = Vertex(2)
    r1.addVertex(vertex)

    Thread.sleep(1)
    r2.removeVertex(vertex)

    Thread.sleep(1)
    r1.addVertex(vertex)

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.contains(vertex))
  }

  test("add the same node on different replicas") {
    val r1 = LWWGraph[Int]()
    val r2 = LWWGraph[Int]()

    val vertex = Vertex(2)
    r1.addVertex(vertex)

    Thread.sleep(1)
    r2.addVertex(vertex)

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.size == 1)
  }

  test("add and remove different nodes on different replicas") {
    val r1 = LWWGraph[Int]()
    val r2 = LWWGraph[Int]()

    val vertex = Vertex(2)
    r1.addVertex(vertex)

    Thread.sleep(1)
    r2.addVertex(vertex)

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.size == 1)
  }
}
