package day10

import day06.main
import readInput

data class Instruction(val time: Int, val change: Int)

fun main() {
    fun parse(input: List<String>) = input.map {
        if (it == "noop") {
            Instruction(1, 0)
        } else {
            Instruction(2, it.split(" ")[1].toInt())
        }
    }

    fun registerValues(input: List<Instruction>) = input.runningFold(listOf(1)) { register, (cycles, change) ->
            val registerValue = register.last()
            buildList {
                repeat(cycles - 1) {
                    add(registerValue)
                }
                add(registerValue + change)
            }
        }.flatten()

    fun part1(input: List<Instruction>) = registerValues(input).let { registerValues ->
        listOf(20, 60, 100, 140, 180, 220).sumOf { cycle -> cycle * registerValues[cycle - 1] }
    }

    fun part2(input: List<Instruction>) = registerValues(input).chunked(40).joinToString("\n") { spritePositions ->
        spritePositions.mapIndexed{ crt, sprite -> if (crt in sprite - 1..sprite + 1) "██" else "  " }.joinToString("")
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
