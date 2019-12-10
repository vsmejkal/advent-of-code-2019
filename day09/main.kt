package day09

import java.io.File
import java.math.BigInteger

fun main() {
    val program = loadProgram("input.txt")

    Computer(program).run(BigInteger.TWO)
}

fun loadProgram(file: String): List<BigInteger> {
    return File(file).readText().trim().split(',').map(String::toBigInteger)
}