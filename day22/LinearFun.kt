package day22

import java.math.BigInteger

class LinearFun(val a: BigInteger, val b: BigInteger) {
    fun apply(x: BigInteger, deckSize: BigInteger): BigInteger {
        return normalize(a * x + b, deckSize)
    }

    fun compose(other: LinearFun, deckSize: BigInteger): LinearFun {
        return LinearFun(
            normalize(a * other.a, deckSize),
            normalize(a * other.b + b, deckSize)
        )
    }

    fun inverse(deckSize: BigInteger): LinearFun {
        return LinearFun(
            normalizedDivision(BigInteger.ONE, a, deckSize),
            normalizedDivision(-b, a, deckSize)
        )
    }

    private fun normalize(position: BigInteger, deckSize: BigInteger): BigInteger {
        return if (position < BigInteger.ZERO) {
            deckSize - normalize(-position, deckSize)
        } else {
            position % deckSize
        }
    }

    private fun normalizedDivision(a: BigInteger, b: BigInteger, deckSize: BigInteger): BigInteger {
        val newA = generateSequence(a) { it + deckSize }.first { it % b == BigInteger.ZERO }

        return normalize(newA / b, deckSize)
    }
}