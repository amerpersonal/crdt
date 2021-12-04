package lww_graph.multiple_replicas

import data.Vertex
import graphs.directed_graph.DirectedGraph
import lww_graph.LWWGraph
import org.scalatest.funsuite.AnyFunSuite

class MergedLWWGraphPathsSuite extends AnyFunSuite {
  test("paths empty for empty LWW graph") {
    val g = LWWGraph[Int]()

    assert(g.findPaths(Vertex(0), Vertex(1)) == Vector.empty)
  }
}
