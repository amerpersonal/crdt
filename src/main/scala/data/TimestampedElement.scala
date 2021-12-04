package data

case class TimestampedElement[A](value: A, timestamp: Long)

object TimestampedElement {
  def apply[A](value: A): TimestampedElement[A] = TimestampedElement(value, System.currentTimeMillis())
}

