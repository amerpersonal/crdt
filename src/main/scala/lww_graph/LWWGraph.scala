package lww_graph

import data.Vertex

import scala.collection.mutable

case class LWWGraph[A](
                        nodes: mutable.Set[Vertex[A]],
                        adjacency: mutable.Map[Vertex[A], Set[Vertex[A]]]
                      
                      )