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

    fun part1(start: Coord, end: Coord, heightmap: Map<Coord, Char>) =
        generateSequence(setOf(start to 'a') to mutableSetOf<Coord>()) { (frontier, seen) ->
            frontier.flatMap { (coord, elevation) ->
                coord.closeNeighbours.filter { it !in seen }.mapNotNull { newCoord ->
                    heightmap[newCoord]?.takeIf { it - elevation <= 1 }?.let { newCoord to it }
                }
            }.toSet().takeIf { end !in seen }?.let { newFrontier ->
                seen += newFrontier.map { it.first }
                newFrontier to seen
            }
        }.count()

    fun part2(end: Coord, heightmap: Map<Coord, Char>) =
        generateSequence(setOf(end to 'z') to mutableSetOf<Coord>()) { (frontier, seen) ->
            frontier.flatMap { (coord, elevation) ->
                coord.closeNeighbours.filter { it !in seen }.mapNotNull { newCoord ->
                    heightmap[newCoord]?.takeIf { elevation - it <= 1 }?.let { newCoord to it }
                }
            }.toSet().takeIf { it.none { (_, elevation) -> elevation == 'a' } }?.let { newFrontier ->
                seen += newFrontier.map { it.first }
                newFrontier to seen
            }
        }.count()

    val (start, end, heightmap) = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(start, end, heightmap))
    println("Part2=\n" + part2(end, heightmap))
}
