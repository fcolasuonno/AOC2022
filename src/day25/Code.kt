package day25

import day06.main
import readInput

fun main() {
    fun part1(input: List<String>) = input.sumOf {
        it.fold(0L) { acc, c ->
            acc * 5 + when (c) {
                '2' -> 2
                '1' -> 1
                '0' -> 0
                '-' -> -1
                '=' -> -2
                else -> throw UnsupportedOperationException()
            }
        }
    }.let {
        var num = it
        buildString {
            while (num > 0) {
                append(
                    when ((num + 2) % 5) {
                        4L -> '2'
                        3L -> '1'
                        2L -> '0'
                        1L -> '-'
                        0L -> '='
                        else -> throw UnsupportedOperationException()
                    }
                )
                num = (num + 2) / 5L
            }
        }.reversed()
    }

    val input = readInput(::main.javaClass.packageName)
    println("Part1=\n" + part1(input))
}
