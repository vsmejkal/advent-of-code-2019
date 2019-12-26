package day18

import java.awt.Point
import kotlin.system.measureTimeMillis


fun main() {
    val maze = Maze.loadFromFile("input2.txt")
    val iterations = 10000
    var minSteps = Int.MAX_VALUE

    val time = measureTimeMillis {
        repeat(iterations) { i ->
            val steps = visitAllKeys(maze)
            if (steps < minSteps) {
                minSteps = steps
                println("Min steps updated: $minSteps")
            }
            reportProgress(i, iterations)
        }
    }

    println("Min steps: $minSteps")
    println("Time: ${1e-3 * time} s")
}

fun reportProgress(step: Int, total: Int) {
    if (step % (total / 100) == 0) {
        println("${100 * step / total}% done")
    }
}

fun visitAllKeys(maze: Maze): Int {
    val reachableKeys = maze.findAll(Tile.ENTRANCE).flatMap { findReachableKeys(maze, it) }
    if (reachableKeys.isEmpty()) {
        return 0
    }

    val key = selectKeyCandidate(reachableKeys)
    val doorPosition = maze.find(key.doorName)

    maze[key.entrance] = Tile.EMPTY
    maze[key.position] = Tile.ENTRANCE
    if (doorPosition != null) {
        maze[doorPosition] = Tile.EMPTY
    }

    val distance = key.distance + visitAllKeys(maze)

    maze[key.entrance] = Tile.ENTRANCE
    maze[key.position] = key.keyName
    if (doorPosition != null) {
        maze[doorPosition] = key.doorName
    }

    return distance
}

fun selectKeyCandidate(keys: List<Key>): Key {
    val proximities = keys.map { 1.0 / it.distance }
    val total = proximities.sum()
    val random = Math.random() * total
    var accumulator = 0.0

    repeat (keys.size) { i ->
        accumulator += proximities[i]
        if (random <= accumulator) {
            return keys[i]
        }
    }

    return keys.last()
}

fun findReachableKeys(maze: Maze, entrance: Point): List<Key> {
    val keys = mutableListOf<Key>()
    val visited = mutableSetOf(entrance)
    val queue = mutableListOf(entrance to 0)

    while (queue.isNotEmpty()) {
        val (position, distance) = queue.removeAt(0)

        for (direction in Direction.values()) {
            val newPosition = direction.move(position)
            val newTile = maze[newPosition] ?: continue
            val newDistance = distance + 1

            if (newTile != Tile.WALL && newPosition !in visited) {
                if (Tile.isKey(newTile)) {
                    keys.add(Key(newTile, newPosition, newDistance, entrance))
                } else if (newTile == Tile.EMPTY) {
                    queue.add(newPosition to newDistance)
                }
                visited.add(newPosition)
            }
        }
    }

    return keys
}