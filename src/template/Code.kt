package template

import readInput

fun main() {
    fun part1(input: List<String>) = input.size

    fun part2(input: List<String>) = input.size

    val input = readInput(::main.javaClass.packageName)
    println(part1(input))
    println(part2(input))
}
