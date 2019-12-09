package day03

import java.io.File
import kotlin.math.abs

typealias Wire = List<Pair<Int, Int>>

fun main() {
    val wires = mutableListOf<Wire>()
    File("input.txt").forEachLine { line ->
        wires.add(parseWireCoords(line))
    }

    val intersections = wires[0].intersect(wires[1])

    val minDistance = intersections.map { abs(it.first) + abs(it.second) }.min()
    println("Minimal intersection distance: $minDistance")

    val minSteps = intersections.map { wires[0].indexOf(it) + wires[1].indexOf(it) + 2 }.min()
    println("Minimal number of steps to intersection: $minSteps")
}

fun parseWireCoords(line: String): Wire {
    val wire = mutableListOf(Pair(0, 0))

    line.split(',').forEach {
        val length = it.drop(1).toInt()
        val stepX = when (it[0]) { 'L' -> -1; 'R' -> 1; else -> 0 }
        val stepY = when (it[0]) { 'U' -> -1; 'D' -> 1; else -> 0 }

        (1..length).map {
            wire.add(Pair(wire.last().first + stepX, wire.last().second + stepY))
        }
    }

    return wire.drop(1)
}

