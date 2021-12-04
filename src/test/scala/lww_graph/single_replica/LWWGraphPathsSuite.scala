package lww_graph.single_replica

import data.Vertex
import graphs.directed_graph.DirectedGraph
import lww_graph.LWWGraph
import org.scalatest.funsuite.AnyFunSuite

class LWWGraphPathsSuite extends AnyFunSuite {

  test("paths empty for empty LWW graph") {
    val g = LWWGraph[Int]()

    assert(g.findPaths(Vertex(0), Vertex(1)) == Vector.empty)
  }

  test("all paths between nodes found correctly in 3 nodes graph populated on different replicas") {
    val (r1, r2) = (LWWGraph[Int](0), LWWGraph[Int]())

    r1.addVertex(Vertex(1))
    r2.addVertex(Vertex(2))

    Thread.sleep(1)
    r2.addEdge(Vertex(1), Vertex(2))
    r1.addEdge(Vertex(0), Vertex(1))

    val merged = LWWGraph.merge(r1, r2)
    assert(merged.findPaths(Vertex(0), Vertex(2)) == Vector(Vector(Vertex(0), Vertex(1), Vertex(2))))
  }

  test("all paths between nodes found correctly in 5 nodes char graphs.graph") {
    val (r1, r2) = (LWWGraph[Int](), LWWGraph[Int]())

    r1.addVertex(Vertex(0))
    r1.addVertex(Vertex(2))
    r2.addVertex(Vertex(3))

    r1.addEdge(Vertex(0), Vertex(1))
    r2.addVertex(Vertex(1))
    r1.addEdge(Vertex(0), Vertex(2))
    r2.addEdge(Vertex(1), Vertex(2))
    r1.addEdge(Vertex(2), Vertex(0))
    r2.addEdge(Vertex(2), Vertex(3))
    r2.addEdge(Vertex(3), Vertex(3))

    val merged = LWWGraph.merge(r1, r2)

    assert(merged.findPaths(Vertex(0), Vertex(3)).sortBy(_.size) == Vector(Vector(Vertex(0), Vertex(2), Vertex(3)), Vector(Vertex(0), Vertex(1), Vertex(2), Vertex(3))))
  }


}
