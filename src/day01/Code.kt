package day01

import grouped
import readInput

fun main() {
    fun part1(input: List<String>) = input.map(String::toIntOrNull).grouped().maxOfOrNull {
        val carriedCalories = it.sum()
        carriedCalories
    }

    fun part2(input: List<String>) = input
        .map(String::toIntOrNull)
        .grouped()
        .map(List<Int>::sum) //calories for each elf
        .sortedDescending()
        .take(3)
        .sum()

    val testInput = readInput(::main.javaClass.packageName, "test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput(::main.javaClass.packageName, "input")
    println(part1(input))
    println(part2(input))
}
