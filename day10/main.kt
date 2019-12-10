package day10

import javafx.geometry.Point2D
import java.awt.Point
import java.io.File
import kotlin.math.atan2

fun main() {
    val asteroids = loadAsteroids("input.txt")
    val bestLocation = asteroids.maxBy { getVisibleAsteroids(it, asteroids).size }!!

    vaporizeAsteroids(asteroids, bestLocation)
}

fun vaporizeAsteroids(asteroids: Set<Point>, location: Point) {
    val otherAsteroids = asteroids.minus(location).toMutableSet()
    var counter = 0

    while (otherAsteroids.isNotEmpty()) {
        getVisibleAsteroids(location, otherAsteroids).sortedBy {
            getAngleToLaser(it, location)
        }.forEach {
            println("The ${++counter}. asteroid to be vaporized is at ${it.x},${it.y}")
            otherAsteroids.remove(it)
        }
    }
}

fun getVisibleAsteroids(origin: Point, asteroids: Set<Point>): Set<Point> {
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

fun isShadowedBy(asteroid1: Point, asteroid2: Point, origin: Point): Boolean {
    if (asteroid1.distanceSq(origin) < asteroid2.distanceSq(origin)) {
        return false
    }

    val p1 = pointToOrigin(asteroid1, origin)
    val p2 = pointToOrigin(asteroid2, origin)

    return  p1.x * p2.x >= 0 && p1.y * p2.y >= 0 && p1.x * p2.y == p2.x * p1.y
}

private fun getAngleToLaser(point: Point, location: Point): Double {
    val laser = Point2D(1e-12, -1.0)
    val target = normalize(pointToOrigin(point, location))

    return atan2(
        (laser.x * target.y + laser.y * target.x),
        (laser.x * target.x - laser.y * target.y)
    )
}

private fun normalize(vec: Point): Point2D {
    val px = vec.x.toDouble()
    val py = vec.y.toDouble()
    val length = Math.sqrt(px * px + py * py)

    return Point2D(px / length, py / length)
}

fun pointToOrigin(point: Point, origin: Point): Point {
    return Point(point.x - origin.x, point.y - origin.y)
}

fun loadAsteroids(file: String): Set<Point> {
    val asteroids = mutableSetOf<Point>()

    for ((y, line) in File(file).readLines().withIndex()) {
        for ((x, symbol) in line.trim().chunked(1).withIndex()) {
            if (symbol == "#") {
                asteroids.add(Point(x, y))
            }
        }
    }

    return asteroids
}