import data.Vertex
import graph.{Graph, GraphOps}
import org.scalatest.FunSuite
import org.scalatest.funsuite.AnyFunSuite

class GraphSuite extends AnyFunSuite {
  test("always pass") {
    assert(List.empty.size == 0)
  }


  test("empty graph has empty size") {
    val g = Graph()

    assert(g.size == 0)
  }

  test("contains on empty graph returns false") {
    val g = Graph[Int]()

    assert(g.contains(Vertex(5)) == false)
  }


  test("contains works correctly before any edges added") {
    val g = Graph[Int](2, 6, 9, 11)

    assert(g.contains(Vertex(5)) == false)
    assert(g.contains(Vertex(6)) == true)
    assert(g.contains(Vertex(2)) == true)
    assert(g.contains(Vertex(10)) == false)
  }


  test("add non existing vertex returns true") {
    val g = Graph[Int]()

    assert(g.addVertex(Vertex(2)))
  }

  test("add existing vertex returns false") {
    val g = Graph[Int](2)

    assert(g.addVertex(Vertex(2)) == false)
  }


  test("remove non existing vertex returns false") {
    val g = Graph[Int]()

    assert(g.removeVertex(Vertex(2)) == false)
  }

  test("remove existing vertex returns true") {
    val g = Graph[Int](2)

    assert(g.addVertex(Vertex(2)) == true)
  }

  test("add non existing vertex works as expected") {
    val g = Graph[Int]()

    g.addVertex(Vertex(2))

    assert(g.contains(Vertex(2)))
  }

  test("remove existing vertex works as expected") {
    val g = Graph[Int](2)

    g.removeVertex(Vertex(2))

    assert(g.contains(Vertex(2)) == false)

  }

  test("remove edge from empty graph returns false") {
    val g = Graph[Int]()

    assert(g.removeEdge(Vertex(5), Vertex(2)) == false)
  }

  // edges
  test("adding edges returns false if one of the nodes does not exists") {
    val g = Graph[Int](2, 6, 9, 11)

    assert(g.addEdge(Vertex(2), Vertex(5)) == false)
  }

  test("adding edges returns true if both nodes exist") {
    val g = Graph[Int](2, 6, 9, 11)

    assert(g.addEdge(Vertex(2), Vertex(6)))
  }

  test("empty graph does not contain the edge") {
    val g = Graph[Int]()

    assert(g.getEdges(Vertex(5)).size == 0)

  }

  test("adding edges works as expected") {
    val g = Graph[Int](2, 6, 9, 11)

    val source = Vertex(2)
    val destination = Vertex(6)
    g.addEdge(source, destination)

    assert(g.getEdges(source).contains(destination))
    assert(g.getEdges(destination).contains(source) == false)
  }

  test("remove edges works as expected") {
    val g = Graph[Int](2, 6, 9, 11)

    val source = Vertex(2)
    val destination = Vertex(6)
    g.addEdge(source, destination)
    g.addEdge(destination, source)

    g.removeEdge(destination, source)

    assert(g.getEdges(source).contains(destination))
    assert(g.getEdges(destination).contains(source) == false)
  }

  test("all paths between nodes found correctly in 3 nodes graph") {
    val g: Graph[Int] = Graph[Int](0, 1, 2)

    g.addEdge(Vertex(0), Vertex(1))
    g.addEdge(Vertex(0), Vertex(2))
    g.addEdge(Vertex(1), Vertex(2))
    g.addEdge(Vertex(2), Vertex(0))

    val paths = g.findPaths(Vertex(0), Vertex(2))

    assert(paths.sortBy(_.size) == Vector(Vector(0, 2), Vector(0, 1, 2)))
  }

  test("all paths between nodes found correctly in 5 nodes char graph") {
    val g: Graph[Int] = Graph(0, 1, 2, 3)

    g.addEdge(Vertex(0), Vertex(1))
    g.addEdge(Vertex(0), Vertex(2))
    g.addEdge(Vertex(1), Vertex(2))
    g.addEdge(Vertex(2), Vertex(0))
    g.addEdge(Vertex(2), Vertex(3))
    g.addEdge(Vertex(3), Vertex(3))

    val paths = g.findPaths(Vertex(0), Vertex(2))

    println(s"paths: ${paths}")

    assert(paths.sortBy(_.size) == Vector(Vector(0, 2), Vector(0, 1, 2)))
  }
}

