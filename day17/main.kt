package day17

import java.awt.Point
import java.io.File
import java.math.BigInteger

typealias Image = Map<Point, Char>

object World {
    const val FREE = '.'
    const val SCAFFOLD = '#'
}

fun main() {
    val program = loadProgram("input.txt")
    val imageData = Robot(program).getCameraData()
    val image = getCameraImage(imageData)
    val path = traverseScaffolding(image)

    val movement = compressPath(path) ?: error("Could not compress path")
    Robot(program).executeMovement(movement)
}

fun compressPath(path: List<String>): List<String>? {
    val maxLength = 20

    for (lenA in 10 downTo 1) for (lenB in 10 downTo 1) {
        var str = path.joinToString(",")

        val tokenA = str.split(",").take(lenA).joinToString(",")
        str = str.replace(tokenA, "").trim(',')

        val tokenB = str.split(",").take(lenB).joinToString(",")
        str = str.replace(tokenB, "").trim(',')

        val tokenC = str.split(",,").first()
        str = str.replace(tokenC, "").trim(',')

        val program = path.joinToString(",")
                .replace(tokenA, "A")
                .replace(tokenB, "B")
                .replace(tokenC, "C")

        val result = listOf(program, tokenA, tokenB, tokenC)

        if (str.isEmpty() && result.all { it.length <= maxLength }) {
            return result
        }
    }

    return null
}

fun traverseScaffolding(image: Image): List<String> {
    var position = getRobotPosition(image)
    var direction = getRobotDirection(image, position)
    val path = mutableListOf<String>()

    while (true) {
        val forward = direction.move(position)
        if (image[forward] == World.SCAFFOLD) {
            val prevNumber = path.lastOrNull()?.toIntOrNull()
            if (prevNumber != null) {
                path[path.lastIndex] = (prevNumber + 1).toString()
            } else {
                path.add("1")
            }
            position = forward
            continue
        }

        val left = direction.left.move(position)
        if (image[left] == World.SCAFFOLD) {
            path.add("L")
            direction = direction.left
            continue
        }

        val right = direction.right.move(position)
        if (image[right] == World.SCAFFOLD) {
            path.add("R")
            direction = direction.right
            continue
        }

        break
    }

    return path
}

fun getRobotDirection(image: Image, position: Point): Direction {
    return mapOf(
        '^' to Direction.UP,
        'v' to Direction.DOWN,
        '<' to Direction.LEFT,
        '>' to Direction.RIGHT
    ).get(image[position])!!
}

fun getRobotPosition(image: Image): Point {
    return image.keys.first { image[it]!! in "^v<>" }
}

fun getCameraImage(data: String): Image {
    return data.trim().split('\n').withIndex().flatMap { (y, row) ->
        row.withIndex().map { (x, value) ->
            Point(x, y) to value
        }
    }.toMap()
}

fun loadProgram(file: String): List<BigInteger> {
    return File(file).readText().trim().split(',').map(String::toBigInteger)
}
