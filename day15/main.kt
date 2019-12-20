package day15

import java.awt.Point
import java.io.File
import java.math.BigInteger

fun main() {
    val program = loadProgram("input.txt")
    val robot = Robot(program)
    val origin = Point(0, 0)
    val map = mutableMapOf(origin to 0)
    val queue = mutableListOf(origin)
    var oxygenPosition: Point? = null

    while (queue.isNotEmpty() && oxygenPosition == null) {
        println(queue)

        val position = queue.removeAt(0)
        moveRobotTo(robot, position, map)

        Robot.Movement.values().forEach { direction ->
            val status = robot.move(direction)
            val newPosition = move(position, direction)
            robot.move(direction.opposite)

            when (status) {
                Robot.Status.WALL -> {}
                Robot.Status.FREE -> {
                    if (newPosition !in map) {
                        map[newPosition] = map[position]!! + 1
                        queue.add(newPosition)
                    }
                }
                Robot.Status.OXYGEN -> {
                    oxygenPosition = newPosition
                }
            }
        }
    }

    println("Oxygen found at $oxygenPosition")
}

fun moveRobotTo(robot: Robot, position: Point, map: Map<Point, Int>) {
    findPathToOrigin(robot.position, map).forEach { direction ->
        robot.move(direction)
    }
    findPathToOrigin(position, map).reversed().forEach { direction ->
        robot.move(direction.opposite)
    }
}

fun findPathToOrigin(from: Point, map: Map<Point, Int>): List<Robot.Movement> {
    val path = mutableListOf<Robot.Movement>()
    var position = from

    while (map[position] != 0) {
        val direction = Robot.Movement.values().minBy {
            map[move(position, it)] ?: Int.MAX_VALUE
        }
        path.add(direction!!)
        position = move(position, direction)
    }

    return path
}

fun move(position: Point, direction: Robot.Movement): Point {
    val (x, y) = Pair(position.x, position.y)

    return when (direction) {
        Robot.Movement.NORTH -> Point(x, y - 1)
        Robot.Movement.SOUTH -> Point(x, y + 1)
        Robot.Movement.WEST -> Point(x - 1, y)
        Robot.Movement.EAST -> Point(x + 1, y)
    }
}

fun loadProgram(file: String): List<BigInteger> {
    return File(file).readText().trim().split(',').map(String::toBigInteger)
}
