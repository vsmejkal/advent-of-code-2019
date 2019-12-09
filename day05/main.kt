package day05

import java.io.File

enum class OpCode(val code: Int) {
    ADD(1),
    MULTIPLY(2),
    READ(3),
    WRITE(4),
    JUMP_IF_TRUE(5),
    JUMP_IF_FALSE(6),
    LESS_THAN(7),
    EQUALS(8)
}

fun main() {
    val program = loadProgram("input.txt")
    val memory = program.toMutableList()

    runIntcode(memory)
}

fun loadProgram(file: String) = File(file).readText().trim().split(',').map(String::toInt)

fun runIntcode(code: MutableList<Int>) {
    var ip = 0

    fun getOpcode(): OpCode? {
        return OpCode.values().firstOrNull { it.code == code[ip] % 100 }
    }

    fun getValue(offset: Int): Int {
        val divisor = listOf(0, 100, 1000, 10000)[offset]
        val immediateMode = (code[ip] / divisor) % 10 == 1

        return if (immediateMode) code[ip + offset] else code[code[ip + offset]]
    }

    while (ip < code.size) {
        when (getOpcode()) {
            OpCode.ADD -> {
                code[code[ip + 3]] = getValue(1) + getValue(2)
                ip += 4
            }
            OpCode.MULTIPLY -> {
                code[code[ip + 3]] = getValue(1) * getValue(2)
                ip += 4
            }
            OpCode.READ -> {
                code[code[ip + 1]] = readLine()!!.toInt()
                ip += 2
            }
            OpCode.WRITE -> {
                print(getValue(1))
                ip += 2
            }
            OpCode.JUMP_IF_TRUE -> {
                if (getValue(1) != 0) {
                    ip = getValue(2)
                } else {
                    ip += 3
                }
            }
            OpCode.JUMP_IF_FALSE -> {
                if (getValue(1) == 0) {
                    ip = getValue(2)
                } else {
                    ip += 3
                }
            }
            OpCode.LESS_THAN -> {
                if (getValue(1) < getValue(2)) {
                    code[code[ip + 3]] = 1
                } else {
                    code[code[ip + 3]] = 0
                }
                ip += 4
            }
            OpCode.EQUALS -> {
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



