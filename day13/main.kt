package day13

import java.awt.Point
import java.io.File
import java.math.BigInteger
import kotlin.math.sign

object Tile {
    const val EMPTY = 0
    const val WALL = 1
    const val BLOCK = 2
    const val PADDLE = 3
    const val BALL = 4
}

fun main() {
    val program = loadProgram("input.txt")
    val game = Computer(program)
    val tiles = mutableMapOf<Point, Int>()
    val ball = Ball()

    game.onInput =  {
        val paddle = getPositionOf(Tile.PADDLE, tiles)
        ball.update(getPositionOf(Tile.BALL, tiles))

        val target = if (ball.direction.y > 0 && paddle.y - ball.position.y < 4) {
            trackBallToPaddle(ball, tiles)
        } else {
            ball.position
        }

//        printGame(tiles)
//        Thread.sleep(5L)

        val joystick = (target.x - paddle.x).sign
        joystick.toBigInteger()
    }

    val output = mutableListOf<Int>()

    game.onOutput = {
        output.add(it.toInt())

        if (output.size == 3) {
            val (x, y, tile) = output
            tiles[Point(x, y)] = tile
            output.clear()
        }
    }

    game.mem[0] = BigInteger.TWO // Insert coin
    game.run()

    printGame(tiles)
}

fun trackBallToPaddle(ball: Ball, tiles: Map<Point, Int>): Point {
    val (position, direction) = ball.copy()
    val paddle = getPositionOf(Tile.PADDLE, tiles)
    val track = mutableListOf<Point>()

    while (position.y < paddle.y) {
        val newPosition = Point(position.x + direction.x, position.y + direction.y)

        if (tiles[newPosition] == Tile.WALL) {
            direction.x = -direction.x
        } else {
            position.location = newPosition
            track.add(newPosition)
        }
    }

    return track.last()
}

fun getPositionOf(tile: Int, tiles: Map<Point, Int>): Point {
    return tiles.keys.first { tiles[it] == tile }
}

fun printGame(tiles: Map<Point, Int>) {
    val maxX = tiles.keys.map { it.x }.max() ?: return
    val maxY = tiles.keys.map { it.y }.max() ?: return

    printScore(tiles)

    for (y in 0..maxY) {
        for (x in 0..maxX) {
            print(when (tiles[Point(x, y)]) {
                Tile.BLOCK -> "#"
                Tile.BALL -> "o"
                Tile.PADDLE -> "^"
                Tile.WALL -> "+"
                else -> " "
            })
        }
        println()
    }
    println("--------------------------------------------")
}

fun printScore(tiles: Map<Point, Int>) {
    val score = tiles.getOrDefault(Point(-1, 0), 0)

    println("Score: $score")
}

fun loadProgram(file: String): List<BigInteger> {
    return File(file).readText().trim().split(',').map(String::toBigInteger)
}
