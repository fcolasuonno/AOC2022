package template

import day06.main
import readInput

fun main() {
    fun part1(input: List<String>) = input.size

    val input = readInput(::main.javaClass.packageName)
    println("Part1=\n" + part1(input))
}
