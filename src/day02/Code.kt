package day02

import readInput

private enum class Outcome(val score: Int) {
    Lost(0), Draw(3), Win(6);
}

private enum class HandShape(val value: Int) {
    Rock(1), Paper(2), Scissor(3)
}

fun main() {
    val game = mapOf(
        (HandShape.Rock to HandShape.Rock) to Outcome.Draw,
        (HandShape.Rock to HandShape.Paper) to Outcome.Win,
        (HandShape.Rock to HandShape.Scissor) to Outcome.Lost,

        (HandShape.Paper to HandShape.Rock) to Outcome.Lost,
        (HandShape.Paper to HandShape.Paper) to Outcome.Draw,
        (HandShape.Paper to HandShape.Scissor) to Outcome.Win,

        (HandShape.Scissor to HandShape.Rock) to Outcome.Win,
        (HandShape.Scissor to HandShape.Paper) to Outcome.Lost,
        (HandShape.Scissor to HandShape.Scissor) to Outcome.Draw,
    )
    val firstColumnMeaning = mapOf(
        "A" to HandShape.Rock,
        "B" to HandShape.Paper,
        "C" to HandShape.Scissor,
    )
    val secondColumnMeaning1 = mapOf(
        "X" to HandShape.Rock,
        "Y" to HandShape.Paper,
        "Z" to HandShape.Scissor,
    )

    fun parse(input: List<String>) = input.map {
        val (other, mine) = it.split(' ')
        other to mine
    }

    fun part1(input: List<String>) = parse(input).sumOf { (other, mine) ->
        val otherHand = firstColumnMeaning.getValue(other)
        val myHand = secondColumnMeaning1.getValue(mine)
        game.getValue(otherHand to myHand).score + myHand.value
    }

    val secondColumnMeaning2 = mapOf(
        "X" to Outcome.Lost,
        "Y" to Outcome.Draw,
        "Z" to Outcome.Win
    )

    fun part2(input: List<String>) = parse(input).sumOf { (other, mine) ->
        val otherHand = firstColumnMeaning.getValue(other)
        val myOutcome = secondColumnMeaning2.getValue(mine)
        val (_, myHand) = game
            .filterKeys { it.first == otherHand }
            .filterValues { it == myOutcome }
            .keys.single()
        myOutcome.score + myHand.value
    }

    val testInput = readInput(::main.javaClass.packageName, "test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput(::main.javaClass.packageName, "input")
    println(part1(input))
    println(part2(input))
}
