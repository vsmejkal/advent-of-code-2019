package day07

import java.io.File

fun main() {
    val program = loadProgram("input.txt")
    val maxOutput = combinationsOf((5..9).toList()).map { runAmplifiers(program, it) }.max()

    println("Max output signal is $maxOutput")
}

fun runAmplifiers(program: List<Int>, phaseSequence: List<Int>): Int {
    val amplifiers = phaseSequence.map { Amplifier(program, it) }
    var input = 0
    var maxOutput = 0

    loop@ while (true) {
        for (amplifier in amplifiers) {
            input = amplifier.run(input) ?: return maxOutput
        }
        maxOutput = maxOf(input, maxOutput)
    }
}

fun combinationsOf(values: List<Int>): Sequence<List<Int>> = sequence {
    if (values.isEmpty()) {
        yield(emptyList())
    } else {
        values.forEach { value ->
            yieldAll(combinationsOf(values.minus(value)).map { it.plus(value) })
        }
    }
}

fun loadProgram(file: String): List<Int> {
    return File(file).readText().trim().split(',').map(String::toInt)
}
