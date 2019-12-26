package day18

import java.awt.Point
import java.io.File

data class Maze(
        val data: MutableList<Char>,
        val width: Int,
        val height: Int
) {
    init {
        assert(width * height == data.size)
    }

    operator fun get(pos: Point) = get(pos.x, pos.y)

    operator fun get(x: Int, y: Int): Char? {
        return data.getOrNull(y * width + x)
    }

    operator fun set(pos: Point, value: Char) = set(pos.x, pos.y, value)

    operator fun set(x: Int, y: Int, value: Char) {
        data[y * width + x] = value
    }

    fun find(value: Char): Point? {
        val index = data.indexOf(value)

        return if (index >= 0) indexToPoint(index) else null
    }

    fun findAll(value: Char): List<Point> {
        return data.withIndex().filter { it.value == value }.map { indexToPoint(it.index) }
    }

    private fun indexToPoint(index: Int) = Point(index % width, index / width)

    override fun toString(): String {
        val builder = StringBuilder()

        repeat(height) { y ->
            builder.append(data.subList(y * width, (y + 1) * width).joinToString(""))
            builder.append('\n')
        }

        return builder.toString()
    }

    companion object {
        fun loadFromFile(file: String): Maze {
            val input = File(file).readText().trim()
            val data = input.filter { !it.isWhitespace() }.toMutableList()
            val width = input.takeWhile { it != '\n' }.length
            val height = data.size / width

            return Maze(data, width, height)
        }
    }
}