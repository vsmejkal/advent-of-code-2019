package day12

import java.io.File


fun main() {
    val file = "input.txt"
    val initialMoons = loadMoons(file)
    val moons = loadMoons(file)
    val periods = mutableListOf(0, 0, 0)
    var step = 0

    while (periods.any { it == 0 }) {
        simulateMotion(moons)
        step += 1

        for (i in 0..2) {
            val isInitialState = moons.zip(initialMoons).all { (m1, m2) ->
                m1.position[i] == m2.position[i] && m1.velocity[i] == m2.velocity[i]
            }
            if (periods[i] == 0 && isInitialState) {
                periods[i] = step
            }
        }
    }

    val totalPeriod = periods.map(Int::toLong).reduce(::lcm)

    println("The system repeats after $totalPeriod steps")
}

fun simulateMotion(moons: List<Moon>) {
    moons.forEach { moon ->
        moons.minus(moon).forEach { otherMoon ->
            moon.applyGravity(otherMoon)
        }
    }
    moons.forEach(Moon::applyVelocity)
}

fun loadMoons(file: String): List<Moon> {
    val regex = "<x=([-\\d]+), y=([-\\d]+), z=([-\\d]+)>".toRegex()

    return File(file).readLines().mapNotNull { line ->
        regex.matchEntire(line)?.destructured?.let { (x, y, z) ->
            Moon(x.toInt(), y.toInt(), z.toInt())
        }
    }
}

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b