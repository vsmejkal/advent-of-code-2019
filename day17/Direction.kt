package day17

import java.awt.Point

enum class Direction { UP, DOWN, LEFT, RIGHT;
    fun move(position: Point): Point {
        val (x, y) = Pair(position.x, position.y)

        return when (this) {
            UP -> Point(x, y - 1)
            DOWN -> Point(x, y + 1)
            LEFT -> Point(x - 1, y)
            RIGHT -> Point(x + 1, y)
        }
    }

    val opposite: Direction
        get() = when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }

    val left: Direction
        get() = when (this) {
            UP -> LEFT
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
        }

    val right: Direction
        get() = this.left.opposite
}
