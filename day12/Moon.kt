package day12

import kotlin.math.abs
import kotlin.math.sign

class Moon(x: Int, y: Int, z: Int) {
    var position = mutableListOf(x, y, z)
    var velocity = mutableListOf(0, 0, 0)

    fun applyGravity(otherMoon: Moon) {
        for (i in 0..2) {
            velocity[i] += (otherMoon.position[i] - position[i]).sign
        }
    }

    fun applyVelocity() {
        for (i in 0..2) {
            position[i] += velocity[i]
        }
    }

    fun getEnergy(): Int {
        return position.sumBy(::abs) * velocity.sumBy(::abs)
    }

    override fun toString(): String {
        val (px, py, pz) = position
        val (vx, vy, vz) = velocity
        return "Moon {pos=<x=$px, y=$py, z=$pz>, vel=<x=$vx, y=$vy, z=$vz>}"
    }
}