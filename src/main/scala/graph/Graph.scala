package graph

import data.Vertex

import scala.collection.mutable

case class Graph[A](nodes: mutable.Set[Vertex[A]], adjacency: mutable.Map[Vertex[A], Set[Vertex[A]]]){

  def size = nodes.size

  def addVertex(vertex: Vertex[A]): Boolean = {
    val added = nodes.add(vertex)
    if (added) adjacency.update(vertex, Set.empty[Vertex[A]])

    added
  }

  def removeVertex(vertex: Vertex[A]): Boolean = {
    val removed = nodes.remove(vertex)
    if (removed) adjacency.remove(vertex)

    removed
  }

  def addEdge(v1: Vertex[A], v2: Vertex[A]) = {
    if (contains(v1) && contains(v2)) {
      adjacency.update(v1, adjacency.getOrElse(v1, Set.empty[Vertex[A]]) + v2)
      true
    }
    else false
  }

  def removeEdge(v1: Vertex[A], v2: Vertex[A]): Boolean = {
    if (contains(v1) && getEdges(v1).contains(v2)) {
      adjacency.update(v1, adjacency.getOrElse(v1, Set.empty[Vertex[A]]) - v2)
      true
    }
    else false
  }

  def contains(v: Vertex[A]) = nodes.contains(v)

  def getEdges(v: Vertex[A]) = adjacency.getOrElse(v, Set.empty)

  def findPaths(start: Vertex[A], end: Vertex[A]): Vector[Vector[Vertex[A]]] = {
    def loop(current: Vertex[A], path: Vector[Vertex[A]]): Vector[Vector[Vertex[A]]]  = {
      if (current == end) Vector(path :+ current)
      else {
        val adj: Vector[Vertex[A]] = path.lastOption.map { l =>
          adjacency.getOrElse(current, Set.empty).toVector.filterNot(path.contains(_))
        }.getOrElse(adjacency.getOrElse(current, Set.empty).toVector)

        if (adj.isEmpty) Vector()
        else adj.flatMap { el =>
          loop(el, path :+ current)
        }
      }
    }

    loop(start, Vector.empty[Vertex[A]])
  }

  //    def dsf() {
  //      def loop(graph: Graph, node: Int, paths: List[List[Int]], visited: List[Int], stack: List[Int]): List[Int] = {
  //        if (stack.isEmpty || visited.lastOption == Some(node)) visited
  //        else {
  //          val next = stack.head
  //
  //          val stackNew = graph.getEdges(node).toList
  //
  //          if (visited.contains(next)) dfsLoop(graph, node, visited, stackNew)
  //          else dfsLoop(graph, node, next :: visited, stackNew)
  //        }
  //      }
  //    }
}


object Graph {

  def apply[A](elements: A*): Graph[A] = {
    val nodes: mutable.Set[Vertex[A]] = elements.map(Vertex(_)).to[mutable.Set]
    val adjacency: mutable.Map[Vertex[A], Set[Vertex[A]]] = mutable.Map(elements.map(Vertex(_) -> Set.empty[Vertex[A]]): _*)

    Graph[A](nodes, adjacency)
  }
}
