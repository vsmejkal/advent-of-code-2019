package day11

import java.awt.Point
import java.awt.image.BufferedImage
import java.io.File
import java.math.BigInteger
import javax.imageio.ImageIO

object Color {
    const val BLACK = 0
    const val WHITE = 1
}

object Direction {
    const val UP = 0
    const val RIGHT = 1
    const val DOWN = 2
    const val LEFT = 3
}

object Turn {
    const val TURN_LEFT = 0
    const val TURN_RIGHT = 1
}

fun main() {
    val program = loadProgram("input.txt")
    val robot = Computer(program)
    var position = Point(0, 0)
    var direction = Direction.UP
    val panels = mutableMapOf<Point, Int>()

    while (true) {
        val inputColor = if (panels.isEmpty()) Color.WHITE else panels[position] ?: Color.BLACK
        val outputColor = robot.run(inputColor.toBigInteger())?.toInt() ?: break
        val turn = robot.run()?.toInt() ?: break

        panels[position] = outputColor
        direction = turnRobot(direction, turn)
        position = moveRobot(position, direction)
    }

    drawImage(panels, "image.png")
}

fun turnRobot(direction: Int, turn: Int): Int {
    return when (turn) {
        Turn.TURN_LEFT -> (direction + 3) % 4
        Turn.TURN_RIGHT -> (direction + 1) % 4
        else -> direction
    }
}

fun moveRobot(position: Point, direction: Int): Point {
    return when (direction) {
        Direction.LEFT -> Point(position.x - 1, position.y)
        Direction.RIGHT -> Point(position.x + 1, position.y)
        Direction.UP -> Point(position.x, position.y - 1)
        Direction.DOWN -> Point(position.x, position.y + 1)
        else -> position
    }
}

fun drawImage(panels: Map<Point, Int>, filename: String) {
    val width = panels.keys.map { it.x }.max()!! + 1
    val height = panels.keys.map { it.y }.max()!! + 1
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    for (x in 0 until width) {
        for (y in 0 until height) {
            image.setRGB(x, y, when (panels[Point(x, y)]) {
                Color.WHITE -> 0xffffff
                else -> 0x000000
            })
        }
    }

    ImageIO.write(image, "png", File(filename))

    println("Image saved to $filename")
}

fun loadProgram(file: String): List<BigInteger> {
    return File(file).readText().trim().split(',').map(String::toBigInteger)
}
