package day23

import Coord
import day06.main
import neighbours
import readInput

fun main() {

    val directionCheck = listOf<(Set<Coord>, Coord) -> Coord?>(
        { s, c ->
            c.copy(second = c.second - 1)
                .takeIf {
                    listOf(it, it.copy(first = it.first - 1), it.copy(first = it.first + 1))
                        .all { it !in s }
                }
        },
        { s, c ->
            c.copy(second = c.second + 1)
                .takeIf {
                    listOf(it, it.copy(first = it.first - 1), it.copy(first = it.first + 1))
                        .all { it !in s }
                }
        },
        { s, c ->
            c.copy(first = c.first - 1)
                .takeIf {
                    listOf(it, it.copy(second = it.second - 1), it.copy(second = it.second + 1))
                        .all { it !in s }
                }
        },
        { s, c ->
            c.copy(first = c.first + 1)
                .takeIf {
                    listOf(it, it.copy(second = it.second - 1), it.copy(second = it.second + 1))
                        .all { it !in s }
                }
        }
    )

    fun parse(input: List<String>) = input.flatMapIndexed { y, s ->
        s.mapIndexedNotNull { x, c -> c.takeIf { it == '#' }?.let { Coord(x, y) } }
    }.toSet()

    fun Set<Coord>.toSequence() = generateSequence(this to 0) { (elves, firstConsidered) ->
        elves.groupBy(keySelector = { elf ->
            (firstConsidered until (firstConsidered + 4))
                .firstNotNullOfOrNull { directionCheck[it % 4](elves, elf) }
                ?.takeIf { elf.neighbours.any { it in elves } }
                ?: elf
        }).flatMap { (newCoord, competingElves) -> if (competingElves.size == 1) listOf(newCoord) else competingElves }
            .toSet().takeIf { it != elves }
            ?.let { it to firstConsidered + 1 }
    }

    fun part1(input: Set<Coord>) = input.toSequence().drop(10).first().first
        .let {
            (it.maxOf(Coord::first) - it.minOf(Coord::first) + 1) *
                    (it.maxOf(Coord::second) - it.minOf(Coord::second) + 1) - it.size
        }


    fun part2(input: Set<Coord>) = input.toSequence().count()

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
