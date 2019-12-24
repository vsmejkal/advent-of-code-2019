package day13

import java.awt.Point

data class Ball(
        val position: Point = Point(-1, -1),
        val direction: Point = Point(1, 1)
) {
    fun update(newPosition: Point) {
        if (position.x > 0) {
            direction.x = newPosition.x - position.x
            direction.y = newPosition.y - position.y
        }
        position.x = newPosition.x
        position.y = newPosition.y
    }
}