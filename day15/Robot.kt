package day15

import java.awt.Point
import java.math.BigInteger

class Robot(program: List<BigInteger>) {
    val position = Point(0, 0)
    private val computer = Computer(program)

    fun move(direction: Movement): Status {
        val directionCode = (direction.ordinal + 1).toBigInteger()
        val statusCode = computer.run(directionCode)

        return Status.values().first { it.ordinal == statusCode?.toInt() }
    }

    enum class Movement { NORTH, SOUTH, WEST, EAST;
        val opposite: Movement
            get() = when (this) {
                NORTH -> SOUTH
                SOUTH -> NORTH
                WEST -> EAST
                EAST -> WEST
            }
    }

    enum class Status { WALL, FREE, OXYGEN }
}