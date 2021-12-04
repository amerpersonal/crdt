package data

case class TimestampedVertex[A](timestamp: Long, vertex: Vertex[A])

