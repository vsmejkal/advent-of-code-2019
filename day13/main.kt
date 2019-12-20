package day13

import java.awt.Point
import java.io.File
import java.math.BigInteger

object Tile {
    const val EMPTY = 0
    const val WALL = 1
    const val BLOCK = 2
    const val PADDLE = 3
    const val BALL = 4
}

object Joystick {
    const val NEUTRAL = 0
    const val LEFT =  -1
    const val RIGHT = 1
}

fun main() {
    val program = loadProgram("input.txt")
    val game = Computer(program.plus(0.toBigInteger()))
    val tiles = mutableMapOf<Point, Int>()

    // Insert coin
    game.mem[0] = 2.toBigInteger()

    for (i in 0..10) {
        while (true) {
            val x = game.run(BigInteger.ONE)?.toInt() ?: break
            val y = game.run()?.toInt() ?: break
            val tile = game.run()?.toInt() ?: break
            tiles[Point(x, y)] = tile
        }
        printTiles(tiles)
    }
}

fun printTiles(tiles: Map<Point, Int>) {
    val maxX = tiles.keys.map { it.x }.max() ?: return
    val maxY = tiles.keys.map { it.y }.max() ?: return

    for (y in 0..maxY) {
        for (x in 0..maxX) {
            print(when (tiles[Point(x, y)]) {
                Tile.BLOCK -> "#"
                Tile.BALL -> "O"
                Tile.PADDLE -> "^"
                Tile.WALL -> "+"
                else -> " "
            })
        }
        println()
    }

    val score = tiles.getOrDefault(Point(-1, 0), 0)

    println("--------------------------------------------")
    println(" Score: $score")
    println()
}

fun loadProgram(file: String): List<BigInteger> {
    return File(file).readText().trim().split(',').map(String::toBigInteger)
}
