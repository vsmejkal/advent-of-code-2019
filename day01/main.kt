package day01

import java.io.File

fun fuelForMass(mass: Int): Int {
    val fuel = mass / 3 - 2

    return if (fuel <= 0) 0 else fuel + fuelForMass(fuel)
}

fun main() {
    var totalFuel = 0

    File("input.txt").forEachLine { line ->
        totalFuel += fuelForMass(line.toInt())
    }

    println("Total fuel: $totalFuel")
}