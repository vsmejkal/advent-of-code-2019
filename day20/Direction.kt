package day20

import java.awt.Point

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun Point.move(direction: Direction): Point {
    val (x, y) = Pair(this.x, this.y)

    return when (direction) {
        Direction.UP -> Point(x, y - 1)
        Direction.DOWN -> Point(x, y + 1)
        Direction.LEFT -> Point(x - 1, y)
        Direction.RIGHT -> Point(x + 1, y)
    }
}