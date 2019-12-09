package day07

class Amplifier(program: List<Int>, phase: Int) {
    private val mem = program.toMutableList()
    private var ip = 0
    private var inputs = listOf(phase)

    fun run(input: Int): Int? {
        inputs = inputs.plus(input)

        while (ip < mem.size) {
            when (getOpcode()) {
                OpCode.ADD -> {
                    mem[mem[ip + 3]] = getValue(1) + getValue(2)
                    ip += 4
                }
                OpCode.MULTIPLY -> {
                    mem[mem[ip + 3]] = getValue(1) * getValue(2)
                    ip += 4
                }
                OpCode.INPUT -> {
                    mem[mem[ip + 1]] = inputs[0]
                    ip += 2
                    inputs = inputs.drop(1)
                }
                OpCode.OUTPUT -> {
                    val output = getValue(1)
                    ip += 2
                    return output
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
                        mem[mem[ip + 3]] = 1
                    } else {
                        mem[mem[ip + 3]] = 0
                    }
                    ip += 4
                }
                OpCode.EQUALS -> {
                    if (getValue(1) == getValue(2)) {
                        mem[mem[ip + 3]] = 1
                    } else {
                        mem[mem[ip + 3]] = 0
                    }
                    ip += 4
                }
                else -> return null
            }
        }

        return null
    }

    private fun getOpcode(): OpCode? {
        val code = mem[ip] % 100
        return OpCode.values().firstOrNull { it.code == code }
    }

    private fun getValue(offset: Int): Int {
        val divisor = listOf(0, 100, 1000, 10000)[offset]
        val immediateMode = (mem[ip] / divisor) % 10 == 1

        return if (immediateMode) mem[ip + offset] else mem[mem[ip + offset]]
    }
}

enum class OpCode(val code: Int) {
    ADD(1),
    MULTIPLY(2),
    INPUT(3),
    OUTPUT(4),
    JUMP_IF_TRUE(5),
    JUMP_IF_FALSE(6),
    LESS_THAN(7),
    EQUALS(8)
}