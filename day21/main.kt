package day21

import java.io.File
import java.math.BigInteger

val script1 = """
    NOT A J
    NOT B T
    AND T J
    NOT C T
    AND T J
    NOT B J
    NOT C T
    OR T J
    AND D J
    NOT A T
    OR T J
    WALK
""".trimIndent().plus('\n')

val script2 = """
    NOT B T
    NOT E J
    AND T J
    NOT C T
    AND D T
    OR T J
    NOT A T
    OR T J
    RUN
""".trimIndent().plus('\n')

fun main() {
    val program = loadProgram("input.txt")
    val computer = Computer(program)
    computer.outputStream = {
        if (it.toInt() < 128) {
            print(it.toInt().toChar())
        } else {
            println("Amount: $it")
        }
    }

    computer.run(script2.map { it.toInt().toBigInteger() })
}

fun loadProgram(file: String): List<BigInteger> {
    return File(file).readText().trim().split(',').map(String::toBigInteger)
}