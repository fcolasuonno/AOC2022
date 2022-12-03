package day02

import readInput

private enum class Outcome(val score: Int) {
    Lost(0), Draw(3), Win(6);
}

private enum class HandShape(val value: Int) {
    Rock(1), Paper(2), Scissor(3)
}

fun <E> Iterable<E>.cartesian() = flatMap { left -> map { right -> left to right } }

fun main() {
    val game = HandShape.values().toList().cartesian().associateWith { (hand1, hand2) ->
        when {
            hand1.value == hand2.value -> Outcome.Draw
            hand1.value % 3 == (hand2.value - 1) -> Outcome.Win
            else -> Outcome.Lost
        }
    }

    fun parse(input: List<String>) = input.map {
        val (other, mine) = it.split(' ')
        other.single() to mine.single()
    }

    fun part1(input: List<String>) = parse(input).sumOf { (other, mine) ->
        val otherHand = HandShape.values()[other - 'A']
        val myHand = HandShape.values()[mine - 'X']
        game.getValue(otherHand to myHand).score + myHand.value
    }

    fun part2(input: List<String>) = parse(input).sumOf { (other, mine) ->
        val otherHand = HandShape.values()[other - 'A']
        val myOutcome = Outcome.values()[mine - 'X']
        val (_, myHand) = game
            .filterKeys { it.first == otherHand }
            .filterValues { it == myOutcome }
            .keys.single()
        myOutcome.score + myHand.value
    }

    val input = readInput(::main.javaClass.packageName)
    println(part1(input))
    println(part2(input))
}
