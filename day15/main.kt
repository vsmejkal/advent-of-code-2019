package day15

import java.awt.Point
import java.io.File
import java.math.BigInteger

fun main() {
    val program = loadProgram("input.txt")
    val robot = Robot(program)
    val map = createMap(robot)
    val oxygenPosition = map.keys.first { map[it] == Robot.Map.OXYGEN }
    val oxygenDistanceMap = createDistanceMap(oxygenPosition, map)

    println("Oxygen system found at $oxygenPosition")
    println("Distance to oxygen system is ${oxygenDistanceMap[Point(0, 0)]}")
    println("Oxygen will fill the area in ${oxygenDistanceMap.values.max()} minutes")
}

fun createMap(robot: Robot): Map<Point, Int> {
    val origin = Point(0, 0)
    val map = mutableMapOf(origin to Robot.Map.FREE)
    val distanceMap = mutableMapOf(origin to 0)
    val queue = mutableListOf(origin)

    while (queue.isNotEmpty()) {
        val position = queue.removeAt(0)
        moveRobotTo(robot, position, distanceMap)

        Robot.Direction.values().forEach { direction ->
            val status = robot.move(direction)
            val newPosition = direction.move(position)

            if (status != Robot.Map.WALL) {
                if (newPosition !in map) {
                    queue.add(newPosition)
                    distanceMap[newPosition] = distanceMap[position]!! + 1
                }
                robot.move(direction.opposite)
            }

            map[newPosition] = status
        }
    }

    return map
}

fun createDistanceMap(origin: Point, map: Map<Point, Int>): Map<Point, Int> {
    val distMap = mutableMapOf(origin to 0)
    val queue = mutableListOf(origin)

    while (queue.isNotEmpty()) {
        val position = queue.removeAt(0)

        Robot.Direction.values().forEach { direction ->
            val newPosition = direction.move(position)

            if (map[newPosition] != Robot.Map.WALL && newPosition !in distMap) {
                distMap[newPosition] = distMap[position]!! + 1
                queue.add(newPosition)
            }
        }
    }

    return distMap
}

fun findPathFrom(from: Point, map: Map<Point, Int>): List<Robot.Direction> {
    val path = mutableListOf<Robot.Direction>()
    var position = from

    while (map[position] != 0) {
        val direction = Robot.Direction.values().minBy {
            map[it.move(position)] ?: Int.MAX_VALUE
        }
        path.add(direction!!)
        position = direction.move(position)
    }

    return path
}

fun moveRobotTo(robot: Robot, position: Point, map: Map<Point, Int>) {
    findPathFrom(robot.position, map).forEach { direction ->
        robot.move(direction)
    }
    findPathFrom(position, map).reversed().forEach { direction ->
        robot.move(direction.opposite)
    }
}

fun loadProgram(file: String): List<BigInteger> {
    return File(file).readText().trim().split(',').map(String::toBigInteger)
}
