package data

case class Vertex[A](value: A) {
  def map[B](f: A => B) = Vertex(f(value))
}

