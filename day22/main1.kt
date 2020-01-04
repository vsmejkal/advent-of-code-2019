package day22a

import java.io.File

private const val DECK_SIZE = 10007

private fun main() {
    val instructions = loadInstructions("input.txt")
    var cards = (0 until DECK_SIZE).toList()

    cards = shuffle(cards, instructions)

    println("Position of card 2019 is ${cards.indexOf(2019)}")
}

private fun shuffle(cards: List<Int>, instructions: List<String>): List<Int> {
    var deck = cards

    for (instruction in instructions) {
        val text = instruction.takeWhile { it.isLetter() || it.isWhitespace() }.trim()
        val param = instruction.drop(text.length).trim().toIntOrNull() ?: 0

        deck = when (text) {
            "deal into new stack" -> deck.reversed()
            "cut" -> cutCards(deck, param)
            "deal with increment" -> dealWithIncrement(deck, param)
            else -> error("Unknown instruction $text")
        }
    }

    return deck
}

private fun cutCards(cards: List<Int>, param: Int): List<Int> {
    return if (param < 0) {
        cards.takeLast(-param).plus(cards.dropLast(-param))
    } else {
        cards.drop(param).plus(cards.take(param))
    }
}

private fun dealWithIncrement(cards: List<Int>, param: Int): List<Int> {
    val result = ArrayList(cards)

    cards.withIndex().forEach { (index, value) ->
        result[(index * param) % cards.size] = value
    }

    return result
}

private fun loadInstructions(file: String): List<String> {
    return File(file).readLines()
}