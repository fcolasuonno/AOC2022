package day12

import Coord
import closeNeighbours
import day06.main
import readInput

fun main() {
    fun parse(input: List<String>): Triple<Coord, Coord, Map<Coord, Char>> {
        val map = input.flatMapIndexed { y, s -> s.mapIndexed { x, c -> Coord(x, y) to c } }.toMap()
        val start = map.entries.first { it.value == 'S' }.key
        val end = map.entries.first { it.value == 'E' }.key
        val heightmap = map.mapValues {
            when (it.value) {
                'S' -> 'a'
                'E' -> 'z'
                else -> it.value
            }
        }
        return Triple(start, end, heightmap)
    }

    fun part1(start: Coord, end: Coord, heightmap: Map<Coord, Char>) = mutableSetOf<Coord>().let { seen ->
        fun Coord.isAccessible(from: Coord) = heightmap[this]?.takeIf { it - heightmap[from]!! <= 1 } != null
        generateSequence(setOf(start)) { frontier ->
            frontier.flatMap { coord ->
                coord.closeNeighbours.filter { newCoord -> newCoord !in seen && newCoord.isAccessible(coord) }
            }.toSet().takeIf { end !in seen }?.also { seen += it }
        }
    }.count()

    fun part2(end: Coord, heightmap: Map<Coord, Char>) = mutableSetOf<Coord>().let { seen ->
        fun Coord.isAccessible(from: Coord) = heightmap[this]?.takeIf { heightmap[from]!! - it <= 1 } != null
        generateSequence(setOf(end)) { frontier ->
            frontier.flatMap { coord ->
                coord.closeNeighbours.filter { newCoord -> newCoord !in seen && newCoord.isAccessible(coord) }
            }.toSet().takeIf { it.none { heightmap[it] == 'a' } }?.also { seen += it }
        }
    }.count()

    val (start, end, heightmap) = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(start, end, heightmap))
    println("Part2=\n" + part2(end, heightmap))
}
