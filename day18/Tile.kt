package day18

object Tile {
    const val ENTRANCE = '@'
    const val EMPTY = '.'
    const val WALL = '#'

    fun isKey(char: Char?) = char in 'a'..'z'
    fun isDoor(char: Char?) = char in 'A'..'Z'
}