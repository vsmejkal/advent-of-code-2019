package day16

import java.io.File
import kotlin.math.abs

fun main() {
    part1()
    part2()
}

fun part1() {
    var signal = loadSignal("input.txt")

    repeat(100) {
        signal = transform(signal)
    }

    println("Part 1: " + signal.take(8).joinToString(""))
}

fun part2() {
    val baseSignal = loadSignal("input.txt")
    val startOffset = baseSignal.take(7).joinToString("").toInt()
    val endOffset = baseSignal.size * 10000
    val signal = (startOffset until endOffset).map { baseSignal[it % baseSignal.size] }.toMutableList()

    repeat(100) {
        for (i in signal.size - 2 downTo 0) {
            signal[i] = (signal[i] + signal[i + 1]) % 10
        }
    }

    println("Part 2: " + signal.take(8).joinToString(""))
}

fun transform(signal: List<Int>): List<Int> {
    val sequence = listOf(0, 1, 0, -1)

    return (1..signal.size).map { repeat ->
        signal.withIndex().map {
            it.value * sequence[((it.index + 1) / repeat) % sequence.size]
        }.sum()
    }.map {
        abs(it) % 10
    }
}

fun loadSignal(file: String): List<Int> {
    return File(file).readText().trim().chunked(1).map(String::toInt)
}
