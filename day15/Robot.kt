package day15

import java.awt.Point
import java.math.BigInteger

class Robot(program: List<BigInteger>) {
    var position = Point(0, 0)
    private val computer = Computer(program)

    fun move(direction: Direction): Int {
        val directionCode = (direction.ordinal + 1).toBigInteger()
        val status = computer.run(directionCode)!!.toInt()

        if (status != Map.WALL) {
            position = direction.move(position)
        }

        return status
    }

    enum class Direction { NORTH, SOUTH, WEST, EAST;
        val opposite: Direction
            get() = when (this) {
                NORTH -> SOUTH
                SOUTH -> NORTH
                WEST -> EAST
                EAST -> WEST
            }

        fun move(position: Point): Point {
            val (x, y) = Pair(position.x, position.y)

            return when (this) {
                NORTH -> Point(x, y - 1)
                SOUTH -> Point(x, y + 1)
                WEST -> Point(x - 1, y)
                EAST -> Point(x + 1, y)
            }
        }
    }

    object Map {
        const val WALL = 0
        const val FREE = 1
        const val OXYGEN = 2
    }
}