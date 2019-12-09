package day06

import java.io.File

val orbits = mutableMapOf<String, String>()

fun main() {
    File("input.txt").forEachLine { line ->
        val objects = line.split(')')
        orbits[objects[1]] = objects[0]
    }

    val totalOrbits = orbits.keys.map { getOrbitParentsOf(it).count() }.sum()

    println("Total orbits: $totalOrbits")

    val youOrbits = getOrbitParentsOf("YOU")
    val santaOrbits = getOrbitParentsOf("SAN")

    val youOrbitsTail = youOrbits.dropWhile { santaOrbits.contains(it) }
    val santaOrbitsTail = santaOrbits.dropWhile { youOrbits.contains(it) }

    println("From YOU orbit to SANTA orbit: ${youOrbitsTail.size + santaOrbitsTail.size}")
}

fun getOrbitParentsOf(objectId: String): List<String> {
    val parentId = orbits[objectId] ?: return emptyList()

    return getOrbitParentsOf(parentId).plus(parentId)
}