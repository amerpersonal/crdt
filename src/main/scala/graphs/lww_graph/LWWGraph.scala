package lww_graph

import data.Bias.Bias
import data.{Bias, TimestampedElement, Vertex}
import graphs.Graph
import scala.collection.mutable

/**
  * Last Write Win graph implementation that support multiple replicas
  * For logic of LWW check https://en.wikipedia.org/wiki/Conflict-free_replicated_data_type
  *
  * @param addNodes        - map where each entry key is node and each entry value is list of timestamped operations when that node is added to graph
  * @param removeNodes     - map where each entry key is node and each entry value is list of timestamped operations when that node is removed from graph
  * @param addAdjacency    - map where each key is source vertex and value is map2
  *                          map2 is map where each key is destination vertex and value is list of timestamped operations when edge from source to destination vertex is added
  * @param removeAdjacency - map where each key is source vertex and value is map2
  *                          map2 is map where each key is destination vertex and value is list of timestamped operations when edge from source to destination vertex  is removed
  * @param bias            - bias for graph, can be add or remove
  *                          in case add and remove od node or edge operations has the same timestamp, decision which one should remain is based on bias
  * @tparam A - type of elements stored as graph nodes
  */
case class LWWGraph[A](
                        addNodes: mutable.Map[Vertex[A], List[TimestampedElement[A]]],
                        removeNodes: mutable.Map[Vertex[A], List[TimestampedElement[A]]],
                        addAdjacency: mutable.Map[Vertex[A], Map[Vertex[A], List[TimestampedElement[A]]]],
                        removeAdjacency: mutable.Map[Vertex[A], Map[Vertex[A], List[TimestampedElement[A]]]],
                        bias: Bias = Bias.Add
                      ) extends Graph[A] {

  import LWWGraph._

  def size = mergeNodes(addNodes, removeNodes).size

  /**
    * Add vertex to graph regardless if it already exists
    * @param vertex
    * @return
    */
  def addVertex(vertex: Vertex[A]): Boolean = {
    val existingAddOperations = addNodes.getOrElse(vertex, List.empty)
    addNodes.update(vertex, TimestampedElement(vertex.value) :: existingAddOperations)

    true
  }

  /**
    * Removes vertex to graph regardless if it exists in graph
    * @param vertex
    * @return
    */
  def removeVertex(vertex: Vertex[A]): Boolean = {
    val existingRemoveOperations = removeNodes.getOrElse(vertex, List.empty)
    removeNodes.update(vertex, TimestampedElement(vertex.value) :: existingRemoveOperations)

    true
  }

  /**
    * Add edge between v1 and v2 regardless if edge already exists
    * @param v1
    * @param v2
    * @return
    */
  def addEdge(v1: Vertex[A], v2: Vertex[A]): Boolean = {
    val adjacencyMap = addAdjacency.getOrElse(v1, Map.empty)
    val currentAdjacency = adjacencyMap.getOrElse(v2, List.empty)
    addAdjacency.update(v1, adjacencyMap.updated(v2, TimestampedElement(v2.value) :: currentAdjacency))

    true
  }

  /**
    * Remove edge between v1 and v2 regardless if it exists
    * @param v1
    * @param v2
    * @return
    */
  def removeEdge(v1: Vertex[A], v2: Vertex[A]): Boolean = {
    val adjacencyMap = removeAdjacency.getOrElse(v1, Map.empty)
    val currentAdjacency = adjacencyMap.getOrElse(v2, List.empty)
    removeAdjacency.update(v1, adjacencyMap.updated(v2, TimestampedElement(v2.value) :: currentAdjacency))

    true
  }

  /**
    * Check if vertex exists in LWW graph
    * Vertex exists in graph
    *   - if it is in the add list and not in remove list or
    *   - or if it is in the add list and in remove list, but add occurs after remove
    *   - or add and remove has the same timestamp and graph is biased towards add
    * @param v
    * @return
    */
  def contains(v: Vertex[A]): Boolean = {
    val addOperations = addNodes.getOrElse(v, List.empty)
    val removeOperations = removeNodes.getOrElse(v, List.empty)

    addOperations.nonEmpty && (
      removeOperations.isEmpty ||
        addOperations.head.timestamp > removeOperations.head.timestamp ||
        addOperations.head.timestamp == removeOperations.head.timestamp && bias == Bias.Add
      )
  }

  /**
    * Get edges from specific node in graph
    * Edge exists between 2 nodes if both nodes exists in graph and :
    *   - edge exists in addAdjacency map and does not exist in removeAdjacency map
    *   - edge exists in addAdjacency map and exists in removeAdjacency map, but item in addAdjacency is newer
    *   - edge exists in addAdjacency map and exists in removeAdjacency map, both items has the same timestamp add graph is biased towards add
    * @param v
    * @return
    */
  def getEdges(v: Vertex[A]): Set[Vertex[A]] = {
    val added = addAdjacency.getOrElse(v, Map.empty)
    val removed: Map[Vertex[A], List[TimestampedElement[A]]] = removeAdjacency.getOrElse(v, Map.empty)


    added.filter { case (vertex, adds) =>
      !removed.contains(vertex) ||
        adds.head.timestamp > removed(vertex).head.timestamp ||
        (adds.head.timestamp == removed(vertex).head.timestamp && bias == Bias.Add)
    }.keySet
  }

}

object LWWGraph {
  def apply[A](elements: A*): LWWGraph[A] = {
    val addNodes = mutable.Map(elements.map(v => (Vertex(v), List(TimestampedElement(v, System.currentTimeMillis)))): _*)

    LWWGraph(
      addNodes,
      mutable.Map.empty[Vertex[A], List[TimestampedElement[A]]],
      mutable.Map.empty[Vertex[A], Map[Vertex[A], List[TimestampedElement[A]]]],
      mutable.Map.empty[Vertex[A], Map[Vertex[A], List[TimestampedElement[A]]]]
    )
  }

  /**
    * Merges add and remove nodes of graph to calculate which nodes exists
    * @param addNodes
    * @param removeNodes
    * @param bias
    * @tparam A
    * @return
    */
  def mergeAddAndRemoveNodes[A](addNodes: mutable.Map[Vertex[A], List[TimestampedElement[A]]],
                                removeNodes: mutable.Map[Vertex[A], List[TimestampedElement[A]]], bias: Bias
                               ): Set[Vertex[A]] = {

    addNodes.filter { case (vertex, adds) =>
      !removeNodes.contains(vertex) ||
        adds.head.timestamp > removeNodes(vertex).head.timestamp ||
        (adds.head.timestamp == removeNodes(vertex).head.timestamp && bias == Bias.Add)
    }.keys.toSet

  }

  /**
    * Merges and sorts operations on 2 graphs
    * @param a - operations grouped by nodes
    * @param b - operations grouped by nodes
    * @param vertex
    * @tparam A
    * @return - union of all timestamped operations on @vertex in @a and @b, ordered by timestamp descending, e.g newest first
    */
  def mergeAndSortOperations[A](a: scala.collection.Map[Vertex[A], List[TimestampedElement[A]]],
                                b: scala.collection.Map[Vertex[A], List[TimestampedElement[A]]],
                                vertex: Vertex[A]): List[TimestampedElement[A]] = {

    (a.getOrElse(vertex, List.empty) ++ b.getOrElse(vertex, List.empty)).sortBy(_.timestamp).reverse
  }

  /**
    * Merges and sorts nodes on 2 graphs
    * @param a - map containing nodes with list of timestamped operations for each node
    * @param b - map containing nodes with list of timestamped operations for each node
    * @tparam A
    * @return - map where each entry consists of node and union of all timestamped operations on that node
    */
  def mergeNodes[A](a: mutable.Map[Vertex[A], List[TimestampedElement[A]]], b: mutable.Map[Vertex[A], List[TimestampedElement[A]]]):
                  mutable.Map[Vertex[A], List[TimestampedElement[A]]] = {


    val nodes: scala.collection.Set[(Vertex[A], List[TimestampedElement[A]])] = (a.keySet ++ b.keySet).map { v =>
      val operations: List[TimestampedElement[A]] = mergeAndSortOperations(a, b, v)

      (v, operations)
    }

    mutable.Map(nodes.toSeq: _*)
  }

  /**
    * Merge adjacency map of 2 graphs
    * @param nodes
    * @param a - adjacency map of first graph
    * @param b - adjacency map of second graph
    * @tparam A
    * @return
    */
  def mergeAdjacency[A](nodes: Set[Vertex[A]],
                     a: scala.collection.Map[Vertex[A], Map[Vertex[A], List[TimestampedElement[A]]]],
                     b: scala.collection.Map[Vertex[A], Map[Vertex[A], List[TimestampedElement[A]]]]
                    ): mutable.Map[Vertex[A], Map[Vertex[A], List[TimestampedElement[A]]]] = {

    val mapLike: Iterable[(Vertex[A], Map[Vertex[A], List[TimestampedElement[A]]])] = nodes.map { v =>
      val aAdjMapForVertex = a.getOrElse(v, Map.empty)
      val bAdjMapForVertex = b.getOrElse(v, Map.empty)

      val vertexAdjacency = (aAdjMapForVertex.keySet ++ bAdjMapForVertex.keySet).map { inner =>
        (inner, mergeAndSortOperations(aAdjMapForVertex, bAdjMapForVertex, inner))
      }

      (v, vertexAdjacency.toMap)
    }

    mutable.Map(mapLike.toSeq: _*)
  }

  /**
    * Merge two replicas on LWWGraph, with the following rules
    * 1. Vertex exists in graph :
    *    - if it is in the add list and not in remove list or
    *    - or if it is in the add list and in remove list, but add occurs after remove
    *    - or add and remove has the same timestamp and graph is biased towards add
    * 2. Edge exists between 2 nodes if both nodes exists in graph and :
    *    - edge exists in addAdjacency map and does not exist in removeAdjacency map
    *    - edge exists in addAdjacency map and exists in removeAdjacency map, but item in addAdjacency is newer
    *    - edge exists in addAdjacency map and exists in removeAdjacency map, both items has the same timestamp add graph is biased towards add
    *
    * @param a
    * @param b
    * @tparam A
    * @return
    */
  def merge[A](a: LWWGraph[A], b: LWWGraph[A]): LWWGraph[A] = {

    val addNodes = mergeNodes(a.addNodes, b.addNodes)
    val removeNodes = mergeNodes(a.removeNodes, b.removeNodes)

    val n = mergeAddAndRemoveNodes(addNodes, removeNodes, a.bias)
    val addAdjacency = mergeAdjacency(n, a.addAdjacency, b.addAdjacency)
    val removeAdjacency = mergeAdjacency(n, a.removeAdjacency, b.removeAdjacency)

    LWWGraph(
      addNodes,
      removeNodes,
      addAdjacency,
      removeAdjacency
    )
  }
}