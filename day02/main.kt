package day02

import java.io.File

fun runIntcode(code: MutableList<Int>): Int {
    loop@ for (i in 0 until code.size step 4) {
        when (code[i]) {
            1 -> code[code[i + 3]] = code[code[i + 1]] + code[code[i + 2]]
            2 -> code[code[i + 3]] = code[code[i + 1]] * code[code[i + 2]]
            else -> break@loop
        }
    }

    return code[0]
}

fun main() {
    val program = File("input.txt").readText().trim().split(',').map(String::toInt)
    val desiredOutput = 19690720

    for (noun in 0..99) {
        for (verb in 0..99) {
            val memory = program.toMutableList()
            memory[1] = noun
            memory[2] = verb

            if (runIntcode(memory) == desiredOutput) {
                println("Noun: $noun, Verb: $verb")
                return
            }
        }
    }

    println("No input found in given range")
}