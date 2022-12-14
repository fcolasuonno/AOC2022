package day14

import Coord
import day06.main
import readInput

fun main() {
    operator fun Coord.rangeTo(other: Coord) = (minOf(first, other.first)..maxOf(first, other.first)).flatMap { x ->
        (minOf(second, other.second)..maxOf(second, other.second)).map { y -> Coord(x, y) }
    }

    fun String.toCoord() = split(",").map(String::toInt).let { (x, y) -> Coord(x, y) }

    fun String.toCorners() = split(" -> ").map(String::toCoord)

    fun parse(input: List<String>): Pair<Set<Coord>, Int> =
        input.flatMap { it.toCorners().zipWithNext { a, b -> a..b }.flatten() }.toSet()
            .let { coord -> coord to coord.maxOf(Coord::second) }

    fun part1(initial: Set<Coord>, max: Int) = generateSequence(initial) { current ->
        generateSequence(Coord(500, 0)) { sand ->
            listOf(
                sand.copy(second = sand.second + 1),
                sand.copy(first = sand.first - 1, second = sand.second + 1),
                sand.copy(first = sand.first + 1, second = sand.second + 1)
            ).firstOrNull {
                it !in current && it.second <= max
            }
        }.last().takeIf {
            it.second < max
        }?.let {
            current + it
        }
    }.count() - 1

    fun part2(initial: Set<Coord>, max: Int) =
        generateSequence(Pair(initial, setOf(Coord(500, 0)))) { (current, newSand) ->
            fun isRock(coord: Coord) = coord in initial || coord.second == (max + 2)
            buildSet {
                newSand.forEach { sand ->
                    sand.copy(second = sand.second + 1).takeUnless(::isRock)?.let(::add)
                    sand.copy(first = sand.first - 1, second = sand.second + 1).takeUnless(::isRock)?.let(::add)
                    sand.copy(first = sand.first + 1, second = sand.second + 1).takeUnless(::isRock)?.let(::add)
                }
            }.takeIf { it.isNotEmpty() }?.let { (current + newSand) to it }
        }.last().first.size - initial.size

    val (input, max) = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input, max))
    println("Part2=\n" + part2(input, max))
}