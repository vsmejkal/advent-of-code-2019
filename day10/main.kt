package day10

import javafx.geometry.Point2D
import java.io.File
import kotlin.math.atan2

fun main() {
    val asteroids = loadAsteroids("input.txt")
    val bestLocation = asteroids.maxBy { getVisibleAsteroids(it, asteroids).size }!!

    vaporizeAsteroids(asteroids, bestLocation)
}

fun vaporizeAsteroids(asteroids: Set<Point2D>, location: Point2D) {
    val otherAsteroids = asteroids.minus(location).toMutableSet()
    var counter = 0

    while (otherAsteroids.isNotEmpty()) {
        getVisibleAsteroids(location, otherAsteroids).sortedBy {
            getAngleToLaser(it, location)
        }.forEach {
            println("${++counter}. asteroid to be vaporized is at ${it.x.toInt()},${it.y.toInt()}")
            otherAsteroids.remove(it)
        }
    }
}

fun getVisibleAsteroids(origin: Point2D, asteroids: Set<Point2D>): Set<Point2D> {
    val otherAsteroids = asteroids.minus(origin)
    val result = otherAsteroids.toMutableSet()

    for (asteroid1 in otherAsteroids) {
        for (asteroid2 in otherAsteroids.minus(asteroid1)) {
            if (isShadowedBy(asteroid1, asteroid2, origin)) {
                result.remove(asteroid1)
            }
        }
    }

    return result
}

fun isShadowedBy(asteroid1: Point2D, asteroid2: Point2D, origin: Point2D): Boolean {
    if (asteroid1.distance(origin) < asteroid2.distance(origin)) {
        return false
    }

    val p1 = asteroid1.subtract(origin)
    val p2 = asteroid2.subtract(origin)

    return  p1.x * p2.x >= 0 && p1.y * p2.y >= 0 && p1.x * p2.y == p2.x * p1.y
}

private fun getAngleToLaser(point: Point2D, location: Point2D): Double {
    val laser = Point2D(1e-12, -1.0).normalize()
    val target = point.subtract(location).normalize()

    return atan2(
        (laser.x * target.y + laser.y * target.x),
        (laser.x * target.x - laser.y * target.y)
    )
}

fun loadAsteroids(file: String): Set<Point2D> {
    val asteroids = mutableSetOf<Point2D>()

    for ((y, line) in File(file).readLines().withIndex()) {
        for ((x, symbol) in line.trim().chunked(1).withIndex()) {
            if (symbol == "#") {
                asteroids.add(Point2D(x.toDouble(), y.toDouble()))
            }
        }
    }

    return asteroids
}