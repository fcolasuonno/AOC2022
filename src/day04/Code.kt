package day04

import readInput


operator fun IntRange.contains(other: IntRange) = first in other && last in other
fun IntRange.overlaps(other: IntRange) = first <= other.last && last >= other.first 
fun String.toIntRange(delimiter: Char = '-') = split(delimiter).map { it.toInt() }.let { it[0]..it[1] }

typealias Day04Type = Pair<IntRange,IntRange>

fun main() {
    val format = """(\d+)-(\d+),(\d+)-(\d+)""".toRegex()
    fun parse(input: List<String>) = input.map { line -> 
        format.matchEntire(line)!!.destructured
            .toList().map(String::toInt)
            .let { (x1, x2, y1, y2) ->
                x1..x2 to y1..y2 
            }
    }

    fun part1(input: List<Day04Type>) = input.count { (first,second) ->
        first in second || second in first
    }

    fun part2(input: List<Day04Type>) = input.count { (first,second) ->
        first.overlaps(second)
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println(part1(input))
    println(part2(input))
}

