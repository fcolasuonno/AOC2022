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

    val input = readInput(::main.javaClass.packageName)
    println(part1(input))
    println(part2(input))
}
