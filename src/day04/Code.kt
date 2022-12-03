package day04

import day02.main
import readInput

fun main() {
    fun part1(input: List<String>) = input.size

    fun part2(input: List<String>) = input.size

    val testInput = readInput(::main.javaClass.packageName, "test")
    check(part1(testInput) == 1)
//    check(part2(testInput) == 1)

    val input = readInput(::main.javaClass.packageName, "input")
    println(part1(input))
    println(part2(input))
}
