package day14

import Coord
import day06.main
import readInput

fun main() {
    fun parse(input: List<String>): Set<Coord> =
        input.flatMap {
            it.split(" -> ").map { c -> c.split(",").map(String::toInt).let { (x, y) -> Coord(x, y) } }.zipWithNext()
                .flatMap { (a, b) ->
                    (minOf(a.first, b.first)..maxOf(a.first, b.first)).flatMap { x ->
                        (minOf(a.second, b.second)..maxOf(
                            a.second,
                            b.second
                        )).map { y -> Coord(x, y) }
                    }
                }
        }.toSet()

    fun part1(input: List<String>) = parse(input).let { initial ->
        val max = initial.map { it.second }.max()
        val start = Coord(500, 0)
        generateSequence(initial) { current ->
            generateSequence(start) { sand ->
                (sand.copy(second = sand.second + 1).takeIf { it !in current } ?: sand.copy(
                    first = sand.first - 1,
                    second = sand.second + 1
                ).takeIf { it !in current } ?: sand.copy(first = sand.first + 1, second = sand.second + 1)
                    .takeIf { it !in current }
                        )?.takeIf { it.second < max + 1 }
            }.last().takeIf {
                it.second < max
            }?.let { current + it }
        }
    }.count() - 1

    fun part2(input: List<String>) = parse(input).let { initial ->
        val max = initial.map { it.second }.max()
        val start = Coord(500, 0)
        generateSequence(initial) { current ->
            generateSequence(start) { sand ->
                sand.copy(second = sand.second + 1).takeIf { it !in current && it.second < (max+2) } ?: sand.copy(
                    first = sand.first - 1,
                    second = sand.second + 1
                ).takeIf { it !in current&& it.second < (max+2) } ?: sand.copy(first = sand.first + 1, second = sand.second + 1)
                    .takeIf { it !in current&& it.second < (max+2) }
            }.last().takeIf {
                it != start
            }?.let { current + it }
        }
    }.count()

    val input = readInput(::main.javaClass.packageName)
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}