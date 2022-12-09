package day09

import Coord
import day06.main
import readInput
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun parse(input: List<String>) = input
        .map { motion ->
            val (dir, amount) = motion.split(" ")
            (0..amount.toInt()).map {
                when (dir) {
                    "R" -> Coord(it, 0)
                    "L" -> Coord(-it, 0)
                    "U" -> Coord(0, it)
                    else -> Coord(0, -it)
                }
            }
        }.reduce { prevSegment, newSegment ->
            prevSegment + newSegment.drop(1).map { (dx, dy) ->
                val (x, y) = prevSegment.last()
                Coord(x + dx, y + dy)
            }
        }

    fun Coord.following(head: Coord): Coord {
        val dx = head.first - first
        val dy = head.second - second
        return if (abs(dx) <= 1 && abs(dy) <= 1) {
            this
        } else {
            copy(first + dx.sign, second + dy.sign)
        }
    }

    fun part1(input: List<Coord>) = buildSet {
        input.fold(Coord(0, 0)) { tail, head ->
            tail.following(head).also { add(it) }
        }
    }.size

    fun part2(input: List<Coord>) = buildSet {
        input.fold(List(9) { Coord(0, 0) }) { tail, head ->
            var prev = head
            tail.map { tailKnot ->
                tailKnot.following(prev).also { prev = it }
            }.also { add(it.last()) }
        }
    }.size

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
