package day05

import grouped
import readInput

data class BoxLocation(val layer: Int, val stackNumber: Int, val value: Char)
data class Movement(val amount: Int, val from: Int, val to: Int)

fun main() {

    val format = """move (\d+) from (\d+) to (\d+)""".toRegex()
    fun parse(input: List<String>) = input.map { it.ifEmpty { null } }.grouped().let { (cranes, movements) ->
        val diagram = cranes.dropLast(1).flatMapIndexed { layer, boxes ->
            boxes.withIndex().filter { it.value.isLetter() }.map { (index, value) ->
                val stackNumber = ((index - 1) / 4) + 1
                BoxLocation(layer, stackNumber, value)
            }
        }
        val initialConfiguration = List(diagram.maxOf { it.stackNumber } + 1) { stackNumber ->
            diagram.filter { it.stackNumber == stackNumber }.sortedBy { it.layer }.map { it.value }.toList()
        }
        val parsedMovements = movements.map { line ->
            format.matchEntire(line)!!.destructured
                .toList().map(String::toInt)
                .let { (amount, from, to) ->
                    Movement(amount, from, to)
                }
        }
        initialConfiguration to parsedMovements
    }

    fun part1(input: Pair<List<List<Char>>, List<Movement>>) = input.let { (initial, movements) ->
        val configuration = initial.map { ArrayDeque(it) }
        movements.forEach { (amount, from, to) ->
            repeat(amount) {
                configuration[to].add(0, configuration[from].removeFirst())
            }
        }
        configuration.drop(1).joinToString("") { it.first().toString() }
    }

    fun part2(input: Pair<List<List<Char>>, List<Movement>>) = input.let { (initial, movements) ->
        val configuration = initial.map { ArrayDeque(it) }
        movements.forEach { (amount, from, to) ->
            configuration[from].take(amount).reversed().forEach {
                configuration[to].add(0, it)
            }
            repeat(amount) {
                configuration[from].removeFirst()
            }
        }
        configuration.drop(1).joinToString("") { it.first().toString() }
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
