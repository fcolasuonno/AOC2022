package day03

import day02.main
import readInput

fun main() {
    fun priority(item: Char) = if (item in 'a'..'z') {
        item - 'a' + 1
    } else {
        item - 'A' + 27
    }

    fun part1(input: List<String>) = input.sumOf { rucksack ->
        val first = rucksack.take(rucksack.length / 2).toSet()
        val second = rucksack.drop(rucksack.length / 2).toSet()
        val common = first.intersect(second).single()
        priority(common)
    }

    fun part2(input: List<String>) = input.map(String::toSet).chunked(3) { group ->
        val badge = group.reduce(Set<Char>::intersect).single()
        priority(badge)
    }.sum()

    val testInput = readInput(::main.javaClass.packageName, "test")

    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput(::main.javaClass.packageName, "input")
    println(part1(input))
    println(part2(input))
}
