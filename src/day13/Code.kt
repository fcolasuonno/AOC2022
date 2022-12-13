package day13

import day06.main
import grouped
import readInput

sealed class Packet : Comparable<Packet>

data class IntPacket(val value: Int) : Packet() {
    override fun compareTo(other: Packet) = if (other is IntPacket) {
        value - other.value
    } else {
        ListPacket(listOf(this)).compareTo(other)
    }
}

data class ListPacket(val contents: List<Packet>) : Packet() {
    override fun compareTo(other: Packet): Int = if (other is ListPacket) {
        contents.zip(other.contents, Packet::compareTo).firstOrNull { it != 0 } ?: (contents.size - other.contents.size)
    } else {
        compareTo(ListPacket(listOf(other)))
    }
}

fun main() {
    fun parseInt(chars: MutableList<Char>) =
        chars.takeWhile(Char::isDigit).fold(0) { acc, c -> acc * 10 + (c - '0') }.also {
            while (chars.first().isDigit()) chars.removeFirst()
        }

    fun parseList(chars: MutableList<Char>): ListPacket = ListPacket(buildList {
        chars.removeFirst()
        while (chars.first() != ']') {
            when (chars.first()) {
                '[' -> add(parseList(chars))
                in '0'..'9' -> add(IntPacket(parseInt(chars)))
                ',' -> chars.removeFirst()
            }
        }
        chars.removeFirst()
    })

    fun parse(input: List<String>) =
        input.map { it.takeIf(String::isNotEmpty)?.let { chars -> parseList(chars.toMutableList()) } }

    fun part1(input: List<ListPacket?>) =
        input.grouped().map { (left, right) -> left < right }.foldIndexed(0) { index, acc, inOrder ->
            if (inOrder) acc + index + 1 else acc
        }

    fun part2(input: List<ListPacket?>) = setOf(
        parseList("[[2]]".toMutableList()),
        parseList("[[6]]".toMutableList())
    ).let { dividers ->
        input.asSequence().filterNotNull().plus(dividers).sortedWith(Packet::compareTo).map { it in dividers }
            .foldIndexed(1) { index, acc, isDivider ->
                if (isDivider) acc * (index + 1) else acc
            }
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
