package day19

import java.awt.Point
import java.io.File
import java.math.BigInteger

const val SHIP_SIZE = 100

val program = loadProgram("input.txt")

fun main() {
    val ship = findPosition() ?: error("Not found")
//    renderShipInBeam(ship)

    println("Ship can fit at position: $ship")
    println("Result: ${ship.x * 10000 + ship.y}")
}

private fun findPosition(): Point? {
    var min = 100
    var max = 10000
    var value = min

    while (max - min > 1) {
        value = (min + max) / 2
        if (fitShipAt(value) == null) {
            min = value
        } else {
            max = value
        }
    }

    for (x in value downTo value - 20) {
        if (fitShipAt(x) != null) {
            value = x
        }
    }

    return fitShipAt(value)
}

fun fitShipAt(x1: Int): Point? {
    val x2 = x1 + SHIP_SIZE - 1
    val y1 = (0..x2).first { y -> isInsideBeam(Point(x2, y)) }
    val y2 = y1 + SHIP_SIZE - 1

    return if (isInsideBeam(Point(x1, y2))) Point(x1, y1) else null
}

fun renderBeam(size: Int) {
    repeat(size) { y ->
        repeat(size) { x ->
            print(if (isInsideBeam(Point(x, y))) '#' else '.')
        }
        println()
    }
}

fun renderShipInBeam(ship: Point) {
    val margin = 5
    val xRange = ship.x until (ship.x + SHIP_SIZE)
    val yRange = ship.y until (ship.y + SHIP_SIZE)

    for (y in yRange.first - margin .. yRange.last + margin) {
        for (x in xRange.first - margin .. xRange.last + margin) {
            if (x in xRange && y in yRange) {
                print('O')
            } else {
                print(if (isInsideBeam(Point(x, y))) '#' else '.')
            }
        }
        println()
    }
}

fun isInsideBeam(position: Point): Boolean {
    val input = listOf(position.x.toBigInteger(), position.y.toBigInteger())

    return Computer(program).run(input)?.toInt() == 1
}

fun loadProgram(file: String): List<BigInteger> {
    return File(file).readText().trim().split(',').map(String::toBigInteger)
}