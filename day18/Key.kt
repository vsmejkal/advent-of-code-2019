package day18

import java.awt.Point

data class Key(
        val keyName: Char,
        val position: Point,
        val distance: Int,
        val entrance: Point
) {
    val doorName: Char
        get() = keyName.toUpperCase()
}