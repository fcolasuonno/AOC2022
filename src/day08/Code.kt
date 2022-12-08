package day08

import Coord
import day06.main
import readInput

data class Forest(val trees: Map<Coord, Int>, val xMax: Int, val yMax: Int) : Iterable<Coord> by trees.keys {
    fun isVisible(coord: Coord): Boolean {
        val (x, y) = coord
        val tree = trees[coord]!!
        return (0 until x).all {
            trees[Coord(it, y)]!! < tree
        } || (0 until y).all {
            trees[Coord(x, it)]!! < tree
        } || ((x + 1) until xMax).all {
            trees[Coord(it, y)]!! < tree
        } || ((y + 1) until yMax).all {
            trees[Coord(x, it)]!! < tree
        }
    }

    fun scenicScore(coord: Coord): Int {
        val (x, y) = coord
        val tree = trees[coord]!!
        val left = ((x - 1) downTo 1).takeWhile {
            trees[Coord(it, y)]!! < tree
        }.size
        val top = ((y - 1) downTo 1).takeWhile {
            trees[Coord(x, it)]!! < tree
        }.size
        val right = ((x + 1) until (xMax - 1)).takeWhile {
            trees[Coord(it, y)]!! < tree
        }.size
        val down = ((y + 1) until (yMax - 1)).takeWhile {
            trees[Coord(x, it)]!! < tree
        }.size
        return (left + 1) * (top + 1) * (right + 1) * (down + 1)
    }
}

fun main() {
    fun parse(input: List<String>) = Forest(
        input.flatMapIndexed { y, s -> s.mapIndexed { x, c -> Coord(x, y) to c.digitToInt() } }.toMap(),
        input.first().length,
        input.size
    )

    fun part1(input: Forest) = input.count {
        input.isVisible(it)
    }

    fun part2(input: Forest) = input.filter { (x, y) -> x != 0 && y != 0 && x != input.xMax - 1 && y != input.yMax - 1 }
        .maxOf {
            input.scenicScore(it)
        }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
