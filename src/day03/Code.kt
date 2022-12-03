package day03

import readInput

fun main() {
    fun priority(item: Char) = if (item in 'a'..'z') {
        item - 'a' + 1
    } else {
        item - 'A' + 27
    }

    fun part1(input: List<String>) = input.sumOf { rucksack ->
        val common = rucksack.chunked(rucksack.length / 2)
            .map(String::toSet)
            .reduce(Set<Char>::intersect)
            .single()
        priority(common)
    }

    fun part2(input: List<String>) = input.map(String::toSet).chunked(3).sumOf { group ->
        val badge = group.reduce(Set<Char>::intersect).single()
        priority(badge)
    }

    val input = readInput(::main.javaClass.packageName)
    println(part1(input))
    println(part2(input))
}
