package day20

import day06.main
import readInput

fun main() {
    fun part1(input: List<String>) = input.map { it.toLong() }.withIndex()
        .toMutableList().apply {
            for (originalIndex in indices) {
                val mixingIndex = indexOfFirst { it.index == originalIndex }
                val mixing = removeAt(mixingIndex)
                var finalIndex = (mixingIndex + mixing.value) % size
                while (finalIndex < 0) {
                    finalIndex += size
                }
                add(finalIndex.toInt(), mixing)
            }
        }.run {
            val zero = indexOfFirst { it.value == 0L }
            (1000..3000 step 1000).sumOf {
                this[(zero + it) % size].value
            }
        }

    fun part2(input: List<String>) = input.map { it.toLong() * 811589153L }.withIndex()
        .toMutableList().apply {
            repeat(10) {
                for (originalIndex in indices) {
                    val mixingIndex = indexOfFirst { it.index == originalIndex }
                    val mixing = removeAt(mixingIndex)
                    var finalIndex = (mixingIndex + mixing.value) % size
                    while (finalIndex < 0) {
                        finalIndex += size
                    }
                    add(finalIndex.toInt(), mixing)
                }
            }
        }.run {
            val zero = indexOfFirst { it.value == 0L }
            (1000..3000 step 1000).sumOf {
                this[(zero + it) % size].value
            }
        }

    val input = readInput(::main.javaClass.packageName)
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}

