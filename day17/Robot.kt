package day17

import java.math.BigInteger
import kotlin.text.StringBuilder

class Robot(program: List<BigInteger>) {
    private val computer = Computer(program)

    fun getCameraData(): String {
        val data = StringBuilder()
        computer.outputStream = { data.append(it.toInt().toChar()) }
        computer.run()

        return data.toString()
    }

    fun executeMovement(movement: List<String>) {
        val iterator = movement.joinToString("\n").plus("\nn\n").iterator()

        computer.mem[0] = BigInteger.TWO
        computer.inputStream = { iterator.next().toInt().toBigInteger() }
        computer.outputStream = {
            if (it.toInt() < 128) {
                print(it.toInt().toChar())
            } else {
                println("Dust collected: $it")
            }
        }
        computer.run()
    }
}