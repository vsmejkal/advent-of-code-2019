package day14

import java.io.File
import kotlin.math.ceil

data class Chemical(
    val name: String,
    val amount: Long
) {
    fun times(value: Long) = Chemical(name, amount * value)
}

data class Reaction(
    val inputs: List<Chemical>,
    val output: Chemical
)

fun main() {
    val reactions = loadReactions("input.txt")

    val oreUnits = orePerFuel(reactions, 1)
    println("We need $oreUnits units of ORE per 1 FUEL")

    val fuelUnits = fuelPerOre(reactions, 1000000000000L)
    println("We can produce $fuelUnits units of FUEL")
}

fun fuelPerOre(reactions: List<Reaction>, availableOre: Long): Long {
    var estimatedFuel = 1.0

    do {
        val requiredOre = orePerFuel(reactions, estimatedFuel.toLong())
        estimatedFuel = ceil(estimatedFuel * availableOre / requiredOre)
    } while (requiredOre < availableOre)

    return (estimatedFuel - 1).toLong()
}

fun orePerFuel(reactions: List<Reaction>, requiredFuel: Long): Long {
    val index = reactions.map { it.output.name to it }.toMap()
    val reactor = mutableListOf(Chemical("FUEL", requiredFuel))
    val spareAmounts = mutableMapOf<String, Long>()
    var ore = 0L

    while (reactor.isNotEmpty()) {
        val chemical = reactor.removeAt(0)
        if (chemical.name == "ORE") {
            ore += chemical.amount
            continue
        }

        val reaction = index[chemical.name] ?: error("Chemical ${chemical.name} not found")
        val spareAmount = spareAmounts.getOrDefault(chemical.name, 0)
        val requiredAmount = chemical.amount - spareAmount
        val producedAmount = reaction.output.amount
        val multiplier = ceil(requiredAmount / producedAmount.toDouble()).toLong()
        val inputs = reaction.inputs.map { it.times(multiplier) }

        spareAmounts[chemical.name] = if (requiredAmount > 0) {
            producedAmount * multiplier - requiredAmount
        } else {
            -requiredAmount
        }

        reactor.addAll(inputs)
    }

    return ore
}

fun loadReactions(file: String): List<Reaction> {
    fun parseChemical(value: String) = Chemical(
        value.substringAfter(' '),
        value.substringBefore(' ').toLong()
    )

    return File(file).readLines().map { line ->
        val (left, right) = line.split(" => ")
        val inputs = left.split(", ").map(::parseChemical)
        val output = parseChemical(right)
        Reaction(inputs, output)
    }
}
