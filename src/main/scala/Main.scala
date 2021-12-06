import data.Vertex
import lww_graph.LWWGraph

object Main {


  def main(args: Array[String]): Unit = {
    val g = LWWGraph[Char]('A', 'B', 'C', 'D', 'E', 'F')

    g.addEdge(Vertex('A'), Vertex('B'))
    g.addEdge(Vertex('A'), Vertex('C'))
    g.addEdge(Vertex('A'), Vertex('E'))
    g.addEdge(Vertex('B'), Vertex('D'))
    g.addEdge(Vertex('B'), Vertex('E'))
    g.addEdge(Vertex('C'), Vertex('E'))
    g.addEdge(Vertex('D'), Vertex('C'))
    g.addEdge(Vertex('D'), Vertex('F'))
    g.addEdge(Vertex('F'), Vertex('C'))

    val paths = g.findPaths(Vertex('A'), Vertex('C'))

    paths.foreach { path =>
      println(path.mkString(" - "))
    }
  }
}
