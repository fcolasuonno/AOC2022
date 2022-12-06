package day06

import readInput

fun main() {
    fun part(input: List<String>, size: Int) = input.single().asSequence()
            .windowed(size)
            .indexOfFirst {
                it.toSet().size == size
            } + size

    val input = readInput(::main.javaClass.packageName)
    println("Part1=\n" + part(input, 4))
    println("Part2=\n" + part(input, 14))
}
