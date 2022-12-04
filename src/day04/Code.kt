package day04

import readInput

operator fun IntRange.contains(other: IntRange) = first in other && last in other
fun IntRange.overlaps(other: IntRange) = first <= other.last && last >= other.first 
fun String.toIntRange(delimiter: Char = '-') = split(delimiter).map { it.toInt() }.let { it[0]..it[1] }

fun main() {

    fun parse(input: List<String>) : List<Pair<IntRange,IntRange>> = input.map { it.split(',') }.map {
        (first,second) -> first.toIntRange() to second.toIntRange()
    }
    fun part1(input: List<String>) = parse(input).count { (first,second) ->
        first in second || second in first
    }

    fun part2(input: List<String>) =  parse(input).count { (first,second) ->
        first.overlaps(second)
    }

    val input = readInput(::main.javaClass.packageName)
    println(part1(input))
    println(part2(input))
}

