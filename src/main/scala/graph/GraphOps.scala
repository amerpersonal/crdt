package graph

import data.Vertex

object GraphOps {
  def addVertex[A](in: Graph[A], vertex: Vertex[A]) = {
    in.copy(nodes = in.nodes + vertex, adjacency = in.adjacency.updated(vertex, Set.empty[Vertex[A]]))
  }

  def removeVertex[A](in: Graph[A], vertex: Vertex[A]) = {
    in.copy(nodes = in.nodes - vertex, adjacency = in.adjacency - vertex)
  }

  def addEdge[A](g: Graph[A], v1: Vertex[A], v2: Vertex[A]): (Graph[A], Boolean) = {
    if (g.contains(v1) && g.contains(v2)) {
      val sourceAdjacency = g.adjacency.getOrElse(v1, Set.empty[Vertex[A]])
      val newGraph = g.copy(adjacency = g.adjacency.updated(v1, sourceAdjacency + v2))
      (newGraph, true)
    }
    else (g, false)
  }

  def removeEdge[A](g: Graph[A], v1: Vertex[A], v2: Vertex[A]): (Graph[A], Boolean) = {
    if (g.contains(v1) && g.getEdges(v1).contains(v2)) {
      val sourceAdjacency = g.adjacency.getOrElse(v1, Set.empty[Vertex[A]])
      val newGraph = g.copy(adjacency = g.adjacency.updated(v1, sourceAdjacency - v2))

      (newGraph, true)
    }
    else (g, false)
  }
}
