package day17

import java.math.BigInteger

class Computer(program: List<BigInteger>) {
    val mem = mutableMapOf<Int, BigInteger>()
    var ip = 0
    var relativeBase = 0
    var inputStream: (() -> BigInteger)? = null
    var outputStream: ((BigInteger) -> Unit)? = null

    init {
        program.withIndex().forEach { mem[it.index] = it.value }
    }

    fun finished() = getOpcode() == OpCode.HALT

    fun run(input: BigInteger? = null): BigInteger? {
        while (ip < mem.size) {
            when (getOpcode()) {
                OpCode.ADD -> {
                    mem[getAddr(3)] = getParam(1) + getParam(2)
                    ip += 4
                }
                OpCode.MULTIPLY -> {
                    mem[getAddr(3)] = getParam(1) * getParam(2)
                    ip += 4
                }
                OpCode.INPUT -> {
                    val inputValue = input ?: inputStream?.invoke() ?: return null
                    mem[getAddr(1)] = inputValue
                    ip += 2
                    print(inputValue.toInt().toChar())
                }
                OpCode.OUTPUT -> {
                    val outputValue = getParam(1)
                    ip += 2
                    outputStream?.invoke(outputValue) ?: return outputValue
                }
                OpCode.JUMP_IF_TRUE -> {
                    ip = if (getParam(1) != BigInteger.ZERO) {
                        getParam(2).toInt()
                    } else {
                        ip + 3
                    }
                }
                OpCode.JUMP_IF_FALSE -> {
                    ip = if (getParam(1) == BigInteger.ZERO) {
                        getParam(2).toInt()
                    } else {
                        ip + 3
                    }
                }
                OpCode.LESS_THAN -> {
                    mem[getAddr(3)] = if (getParam(1) < getParam(2)) {
                        BigInteger.ONE
                    } else {
                        BigInteger.ZERO
                    }
                    ip += 4
                }
                OpCode.EQUALS -> {
                    mem[getAddr(3)] = if (getParam(1) == getParam(2)) {
                        BigInteger.ONE
                    } else {
                        BigInteger.ZERO
                    }
                    ip += 4
                }
                OpCode.ADJUST_RELATIVE_BASE -> {
                    relativeBase += getParam(1).toInt()
                    ip += 2
                }
                OpCode.HALT -> {
                    return null
                }
            }
        }

        return null
    }

    private fun getOpcode(): OpCode? {
        val code = mem[ip]!!.toInt() % 100
        return OpCode.values().firstOrNull { it.code == code }
    }

    private fun getMode(offset: Int): Mode {
        val divisor = listOf(0, 100, 1000, 10000)[offset]
        val modeId = (mem[ip]!!.toInt() / divisor) % 10
        return Mode.values().first { it.code == modeId }
    }

    private fun getAddr(offset: Int): Int {
        val value = ip + offset

        return when (getMode(offset)) {
            Mode.POSITION -> mem[value]!!.toInt()
            Mode.IMMEDIATE -> value
            Mode.RELATIVE -> mem[value]!!.toInt() + relativeBase
        }
    }

    private fun getParam(offset: Int): BigInteger {
        return mem[getAddr(offset)] ?: BigInteger.ZERO
    }

    enum class OpCode(val code: Int) {
        ADD(1),
        MULTIPLY(2),
        INPUT(3),
        OUTPUT(4),
        JUMP_IF_TRUE(5),
        JUMP_IF_FALSE(6),
        LESS_THAN(7),
        EQUALS(8),
        ADJUST_RELATIVE_BASE(9),
        HALT(99)
    }

    enum class Mode(val code: Int) {
        POSITION(0),
        IMMEDIATE(1),
        RELATIVE(2)
    }
}