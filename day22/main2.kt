package day22b

import java.io.File

private const val DECK_SIZE = 119315717514047L
private const val REPEAT = 101741582076661L
private const val FINAL_POSITION = 2020L

private fun main() {
    val instructions = loadInstructions("input.txt")
    var position = FINAL_POSITION

    for (i in 1 .. REPEAT) {
        val newPosition = reverseMapping(position, instructions)
        println("$i: ${position - newPosition}")
        position = newPosition
    }

//    println(dealWithIncrement((0 until size).toList(), 5))
//    println((0 until size).map { reverseDealWithIncrement(it.toLong(), 5) })
}

private fun reverseMapping(position: Long, instructions: List<String>): Long {
    var index = position

    for (instruction in instructions.reversed()) {
        val text = instruction.takeWhile { it.isLetter() || it.isWhitespace() }.trim()
        val param = instruction.drop(text.length).trim().toIntOrNull() ?: 0

        index = when (text) {
            "deal into new stack" -> {
                DECK_SIZE - index - 1
            }
            "cut" -> {
                (index + param + DECK_SIZE) % DECK_SIZE
            }
            "deal with increment" -> {
                (0 until param).map { DECK_SIZE * it + position }.first { it % param == 0L } / param
            }
            else -> error("Unknown instruction $text")
        }
    }

    return index
}

private fun dealWithIncrement(cards: List<Int>, param: Int): List<Int> {
    val result = ArrayList(cards)

    cards.withIndex().forEach { (index, value) ->
        result[(index * param) % cards.size] = value
    }

    return result
}

private fun loadInstructions(file: String): List<String> {
    return File(file).readLines()
}