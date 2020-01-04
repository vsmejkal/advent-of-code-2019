package day22

import java.io.File
import java.math.BigInteger

/**
 * Based on solution by sasa1977
 *
 * https://github.com/sasa1977/aoc/
 */

fun main() {
    val instructions = File("input.txt").readLines()
    part1(instructions)
    part2(instructions)
}

fun part1(instructions: List<String>) {
    val deckSize = 10_007.toBigInteger()
    val card = 2019.toBigInteger()
    val position = createFunctions(instructions, deckSize)
        .reduce { fn1, fn2 -> fn2.compose(fn1, deckSize) }
        .apply(card, deckSize)

    println("Position of $card is $position")
}

fun part2(instructions: List<String>) {
    val deckSize = 119_315_717_514_047.toBigInteger()
    val steps = 101_741_582_076_661.toBigInteger()
    val position = 2020.toBigInteger()
    val card = createFunctions(instructions, deckSize)
        .map { it.inverse(deckSize) }
        .reduce { fn1, fn2 -> fn1.compose(fn2, deckSize) }
        .let { applyManyTimes(it, steps, deckSize) }
        .apply(position, deckSize)

    println("Card at position $position is $card")
}

fun applyManyTimes(function: LinearFun, count: BigInteger, deckSize: BigInteger): LinearFun {
    val binaryCount = count.toString(2).map { it.toString().toInt() }
    val functions = generateSequence(function) { it.compose(it, deckSize) }

    return binaryCount
        .reversed()
        .zip(functions.take(binaryCount.size).toList())
        .mapNotNull { (digit, fn) -> if (digit == 1) fn else null }
        .reduce { fn1, fn2 -> fn2.compose(fn1, deckSize) }
}

fun createFunctions(instructions: List<String>, deckSize: BigInteger): List<LinearFun> {
    return instructions.map { instruction ->
        val text = instruction.takeWhile { it.isLetter() || it.isWhitespace() }.trim()
        val param = instruction.drop(text.length).trim().toBigIntegerOrNull() ?: BigInteger.ZERO

        when (text) {
            "deal into new stack" -> LinearFun(-BigInteger.ONE, deckSize - BigInteger.ONE)
            "cut" -> LinearFun(BigInteger.ONE, -param)
            "deal with increment" -> LinearFun(param, BigInteger.ZERO)
            else -> error("Unknown instruction '$text'")
        }
    }
}
