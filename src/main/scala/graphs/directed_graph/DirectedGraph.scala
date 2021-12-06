package graphs.directed_graph

import data.Vertex
import graphs.Graph
import scala.collection.mutable

/**
  * Directed graph implementation
  * Graph edges does not contain weight
  * @param nodes
  * @param adjacency
  * @tparam A - type of elements stored as graph nodes
  */
case class DirectedGraph[A](
                             nodes: mutable.Set[Vertex[A]],
                             adjacency: mutable.Map[Vertex[A], Set[Vertex[A]]]) extends Graph[A] {

  def size = nodes.size

  /**
    * Adds vertex to graph.
    * @param vertex
    * @return true if added vertex does not exists in a graph; otherwise false
    */
  def addVertex(vertex: Vertex[A]): Boolean = {
    val added = nodes.add(vertex)
    if (added) adjacency.update(vertex, Set.empty[Vertex[A]])

    added
  }

  /**
    * Removed vertex from a graph
    * @param vertex
    * @return true if added vertex exists in a graph, ; otherwise false
    */
  def removeVertex(vertex: Vertex[A]): Boolean = {
    val removed = nodes.remove(vertex)
    if (removed) adjacency.remove(vertex)

    removed
  }

  /**
    * Adds edge between 2 nodes in graph
    * @param v1
    * @param v2
    * @return true if edge is not already added between nodes; otherwise false
    */
  def addEdge(v1: Vertex[A], v2: Vertex[A]) = {
    if (contains(v1) && contains(v2)) {
      adjacency.update(v1, adjacency.getOrElse(v1, Set.empty[Vertex[A]]) + v2)
      true
    }
    else false
  }

  /**
    * Removes edge between 2 nodes in graph
    * @param v1
    * @param v2
    * @return true if edge exists between nodes; otherwise false
    */
  def removeEdge(v1: Vertex[A], v2: Vertex[A]): Boolean = {
    if (contains(v1) && getEdges(v1).contains(v2)) {
      adjacency.update(v1, adjacency.getOrElse(v1, Set.empty[Vertex[A]]) - v2)
      true
    }
    else false
  }

  /**
    * Check if node contained in a graph
    * @param v
    * @return
    */
  def contains(v: Vertex[A]) = nodes.contains(v)

  /**
    * Get all edges on specific node in a graph
    * @param v
    * @return
    */
  def getEdges(v: Vertex[A]) = adjacency.getOrElse(v, Set.empty)
}


object DirectedGraph {
  def apply[A](elements: A*): DirectedGraph[A] = {
    val nodes: mutable.Set[Vertex[A]] = elements.map(Vertex(_)).to[mutable.Set]
    val adjacency: mutable.Map[Vertex[A], Set[Vertex[A]]] = mutable.Map(elements.map(Vertex(_) -> Set.empty[Vertex[A]]): _*)

    DirectedGraph[A](nodes, adjacency)
  }
}
