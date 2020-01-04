package day22

import java.io.File

class LinearFun(val a: Long, val b: Long) {
    fun apply(x: Long, deckSize: Long): Long {
        return normalize(a * x + b, deckSize)
    }

    fun compose(other: LinearFun): LinearFun {
        normalize(ga * fa, deck_size), normalize(ga * fb + gb, deck_size)
    }

    fun inverse(): LinearFun {

    }
}

fun main() {
    val instructions = File("input.txt").readLines()
    part1(instructions)
    part2(instructions)
}

fun part1(instructions: List<String>) {
    val deckSize = 10_007L
    val functions = createFunctions(instructions, deckSize)
    val shuffleFun = functions.reduce { f1, f2 -> f1.compose(f2) }
    val position = shuffleFun.apply(2019, deckSize)

    println("Position of 2019 is $position\n")
}

fun part2(instructions: List<String>) {
    val deckSize = 119_315_717_514_047L
    val steps = 101_741_582_076_661L
}

fun createFunctions(instructions: List<String>, deckSize: Long): List<LinearFun> {
    return instructions.map { instruction ->
        val text = instruction.takeWhile { it.isLetter() || it.isWhitespace() }.trim()
        val param = instruction.drop(text.length).trim().toLongOrNull() ?: 0

        when (text) {
            "deal into new stack" -> LinearFun(-1, deckSize - 1)
            "cut" -> LinearFun(1, -param)
            "deal with increment" -> LinearFun(param, 0)
            else -> error("Unknown instruction '$text'")
        }
    }
}

fun normalize(position: Long, deckSize: Long): Long {
    return if (position < 0) {
        deckSize - normalize(-position, deckSize)
    } else {
        position % deckSize
    }
}
