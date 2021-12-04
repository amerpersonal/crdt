package graphs

import data.Vertex

/**
  * Base graph implementation for any kind of graph
  * @tparam A - type of elements stored as graph nodes
  */
trait Graph[A] {
  def addVertex(vertex: Vertex[A]): Boolean

  def removeVertex(vertex: Vertex[A]): Boolean

  def addEdge(v1: Vertex[A], v2: Vertex[A]): Boolean

  def removeEdge(v1: Vertex[A], v2: Vertex[A]): Boolean

  def contains(v: Vertex[A]): Boolean

  def getEdges(v: Vertex[A]): Set[Vertex[A]]

  /**
    * Find all paths between node @start and node @end in the graph
    * @param start -
    * @param end -
    * @return
    */
  def findPaths(start: Vertex[A], end: Vertex[A]): Vector[Vector[Vertex[A]]] = {
    def loop(current: Vertex[A], path: Vector[Vertex[A]]): Vector[Vector[Vertex[A]]]  = {
      if (current == end) Vector(path :+ current)
      else {
        val edges = getEdges(current)

        val adj: Vector[Vertex[A]] = path.lastOption.map { l =>
          edges.filterNot(path.contains)
        }.getOrElse(edges).toVector

        if (adj.isEmpty) Vector()
        else adj.flatMap(loop(_, path :+ current))
      }
    }

    loop(start, Vector.empty[Vertex[A]])
  }
}
