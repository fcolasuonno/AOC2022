package day12

import Coord
import day06.main
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val map = input.flatMapIndexed { y, s -> s.mapIndexed { x, c -> Coord(x, y) to c } }.toMap()
        val start = map.entries.first { it.value == 'S' }.key
        val end = map.entries.first { it.value == 'E' }.key
        val map1 = map.filterKeys { it != start && it != end }.mapValues { assert(it.value in 'a'..'z')
            it.value-'a' }
        System.err.println(map1)
        generateSequence(listOf( start to 0)) {it
        }
        return map1.size
    }

    fun part2(input: List<String>) = input.size

    val input = readInput(::main.javaClass.packageName)
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
