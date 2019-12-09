package day04

fun isValidPassword(number: Int): Boolean {
    val digits = number.toString().map { it.toString().toInt() }
    val isIncreasing = digits == digits.sorted()
    val containsCouple = digits.groupingBy { it }.eachCount().values.any { it == 2 }

    return isIncreasing && containsCouple
}

fun main() {
    val start = 236491
    val end = 713787
    val passwords = (start..end).count(::isValidPassword)

    println("There are $passwords valid passwords")
}