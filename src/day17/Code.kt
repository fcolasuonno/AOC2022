package day17

import Coord
import day06.main
import readInput
import java.util.*

fun main() {
    fun Set<Coord>.moveRight() = buildSet { this@moveRight.forEach { add(it.copy(first = it.first + 1)) } }
    fun Set<Coord>.moveLeft() = buildSet { this@moveLeft.forEach { add(it.copy(first = it.first - 1)) } }
    fun Set<Coord>.moveDown() = buildSet { this@moveDown.forEach { add(it.copy(second = it.second - 1)) } }
    fun Set<Coord>.moveUp(amount: Int) = buildSet { this@moveUp.forEach { add(it.copy(second = it.second + amount)) } }

    val rockTypes = listOf(
        //-
        (0..3).map { Coord(2 + it, 3) },
        //+
        ((0..2).map { Coord(2 + it, 4) } + Coord(3, 3) + Coord(3, 5)),
        //L
        ((0..2).map { Coord(2 + it, 3) } + Coord(4, 4) + Coord(4, 5)),
        //|
        (0..3).map { Coord(2, 3 + it) },
        // #
        ((0..1).map { Coord(2 + it, 3) } + (0..1).map { Coord(2 + it, 4) })
    ).map { it.toSet() }

    data class Simulation(val input: String) {
        val chamber = TreeSet(compareBy(Coord::second, Coord::first))
        val rockSequence = generateSequence(0) { it + 1 }.map { rockTypes[it % rockTypes.size] }.iterator()
        val directions = generateSequence(0) { it + 1 }.map {
            if (input[it % input.length] == '>') Set<Coord>::moveRight else Set<Coord>::moveLeft
        }.iterator()

        fun checkCollision(rock: Set<Coord>) = rock.takeIf {
            it.intersect(chamber).isEmpty() && it.all { (rockX, rockY) -> rockY >= 0 && rockX in 0..6 }
        }

        val topRow
            get() = chamber.lastOrNull()?.let { it.second + 1 } ?: 0

        fun simulate() = generateSequence {
            generateSequence(rockSequence.next().moveUp(topRow) to true) { (currentRock, sideStep) ->
                if (sideStep) {
                    checkCollision(directions.next()(currentRock)) ?: currentRock
                } else {
                    checkCollision(currentRock.moveDown())
                }?.let { it to !sideStep }
            }.last().let { (rocks, _) ->
                chamber.addAll(rocks)
            }
            chamber.last().second + 1
        }
    }

    fun part1(input: String, times: Int) = Simulation(input).simulate().take(times).last()

    fun part2(input: String, times: Long) = Simulation(input).simulate().take(4000).toList().let { height ->
        (1..height.size).first {
            val lastWindow = height.takeLast(500)
            val previousWindow = height.dropLast(it).takeLast(500)
            lastWindow.zip(previousWindow, Int::minus).distinct().size == 1
        }.let { modulo ->
            val repetitions = (times - height.size) / modulo + 1
            val index = (times - 1 - (repetitions * modulo)).toInt()
            val step = height[index] - height[index - modulo]
            height[index] + repetitions * step
        }
    }

    val input = readInput(::main.javaClass.packageName)
    println("Part1=\n" + part1(input.first(), 2022))
    println("Part2=\n" + part2(input.first(), 1000000000000L))
}