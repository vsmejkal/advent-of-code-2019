package day20

import java.awt.Point
import java.io.File

typealias Maze = Map<Point, Char>

object Tile {
    const val EMPTY = '.'
    const val WALL = '#'
}

data class Portal(
        val name: String,
        val target: Point,
        val isOuter: Boolean
)

fun main() {
    val maze = loadMaze("input.txt")

    val steps = findShortestPath(maze)
    println("Shortest path (part 1): $steps")

    val stepsRecursive = findShortestPathRecursive(maze)
    println("Shortest path (part 2): $stepsRecursive")
}

fun findShortestPathRecursive(maze: Maze): Int {
    val start = maze.keys.first { getPortalName(maze, it) == "AA" }
    val finish = maze.keys.first { getPortalName(maze, it) == "ZZ" }
    val portalMap = createPortalMap(maze)
    val portals = mutableListOf(Triple(start, 0, 0))

    while (portals.isNotEmpty()) {
        val (entry, entryDistance, level) = portals.removeAt(0)
        val queue = mutableListOf(entry to entryDistance)
        val visited = mutableSetOf(start)

        while (queue.isNotEmpty()) {
            val (position, distance) = queue.removeAt(0)

            if (position == finish && level == 0) {
                return distance
            }

            if (position != entry && position in portalMap) {
                val exit = portalMap[position]!!
                val exitDistance = distance + 1
                val exitLevel = if (exit.isOuter) level - 1 else level + 1

                if (exitLevel >= 0) {
                    portals.add(Triple(exit.target, exitDistance, exitLevel))
                }
                continue
            }

            for (direction in Direction.values()) {
                val newPosition = position.move(direction)
                val newTile = maze[newPosition] ?: continue
                val newDistance = distance + 1

                if (newTile == Tile.EMPTY && newPosition !in visited) {
                    queue.add(newPosition to newDistance)
                    visited.add(newPosition)
                }
            }
        }
    }

    return 0
}

fun findShortestPath(maze: Maze): Int {
    val start = maze.keys.first { getPortalName(maze, it) == "AA" }
    val finish = maze.keys.first { getPortalName(maze, it) == "ZZ" }
    val portalMap = createPortalMap(maze)
    val queue = mutableListOf(start to 0)
    val visited = mutableSetOf(start)

    while (queue.isNotEmpty()) {
        var (position, distance) = queue.removeAt(0)

        if (position == finish) {
            return distance
        }

        if (position in portalMap) {
            position = portalMap[position]?.target!!
            distance += 1
        }

        for (direction in Direction.values()) {
            val newPosition = position.move(direction)
            val newTile = maze[newPosition] ?: continue
            val newDistance = distance + 1

            if (newTile == Tile.EMPTY && newPosition !in visited) {
                queue.add(newPosition to newDistance)
                visited.add(newPosition)
            }
        }
    }

    return 0
}

fun createPortalMap(maze: Maze): Map<Point, Portal> {
    val portals = mutableMapOf<String, Point>()
    val portalMap = mutableMapOf<Point, Portal>()

    for (position1 in maze.keys) {
        val label = getPortalName(maze, position1) ?: continue
        if (label in portals) {
            val position2 = portals.remove(label)!!
            portalMap[position1] = Portal(label, position2, isOuterCircle(maze, position1))
            portalMap[position2] = Portal(label, position1, isOuterCircle(maze, position2))
        } else {
            portals[label] = position1
        }
    }

    return portalMap.toMap()
}

fun isOuterCircle(maze: Maze, position: Point): Boolean {
    val topLeft = maze.keys.minBy { it.x + it.y }!!
    val bottomRight = maze.keys.maxBy { it.x + it.y }!!

    return position.x in setOf(topLeft.x, bottomRight.x) || position.y in setOf(topLeft.y, bottomRight.y)
}

fun getPortalName(maze: Maze, position: Point): String? {
    val namePattern = "[A-Z]{2}".toRegex()

    if (maze[position] != Tile.EMPTY) return null

    return Direction.values().map { direction ->
        val char1 = maze[position.move(direction)]
        val char2 = maze[position.move(direction).move(direction)]
        when (direction) {
            Direction.RIGHT, Direction.DOWN -> "$char1$char2"
            else -> "$char2$char1"
        }
    }.firstOrNull {
        it.matches(namePattern)
    }
}

fun loadMaze(file: String): Maze {
    val maze = mutableMapOf<Point, Char>()

    for ((y, line) in File(file).readLines().withIndex()) {
        for ((x, tile) in line.withIndex()) {
            if (!tile.isWhitespace()) {
                maze[Point(x, y)] = tile
            }
        }
    }

    return maze
}