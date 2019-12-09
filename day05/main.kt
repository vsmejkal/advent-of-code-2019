package day05

import java.io.File

fun main() {
    val program = loadProgram("input.txt")
    val memory = program.toMutableList()

    runIntcode(memory)
}

fun loadProgram(file: String) = File(file).readText().trim().split(',').map(String::toInt)

fun runIntcode(code: MutableList<Int>) {
    var ip = 0

    fun getOpcode(): Int {
        return code[ip] % 100
    }

    fun getValue(offset: Int): Int {
        val divisor = listOf(0, 100, 1000, 10000)[offset]
        val immediateMode = (code[ip] / divisor) % 10 == 1

        return if (immediateMode) code[ip + offset] else code[code[ip + offset]]
    }

    while (ip < code.size) {
        when (getOpcode()) {
            1 -> {
                code[code[ip + 3]] = getValue(1) + getValue(2)
                ip += 4
            }
            2 -> {
                code[code[ip + 3]] = getValue(1) * getValue(2)
                ip += 4
            }
            3 -> {
                code[code[ip + 1]] = readLine()!!.toInt()
                ip += 2
            }
            4 -> {
                print(getValue(1))
                ip += 2
            }
            5 -> {
                if (getValue(1) != 0) {
                    ip = getValue(2)
                } else {
                    ip += 3
                }
            }
            6 -> {
                if (getValue(1) == 0) {
                    ip = getValue(2)
                } else {
                    ip += 3
                }
            }
            7 -> {
                if (getValue(1) < getValue(2)) {
                    code[code[ip + 3]] = 1
                } else {
                    code[code[ip + 3]] = 0
                }
                ip += 4
            }
            8 -> {
                if (getValue(1) == getValue(2)) {
                    code[code[ip + 3]] = 1
                } else {
                    code[code[ip + 3]] = 0
                }
                ip += 4
            }
            else -> return
        }
    }
}



