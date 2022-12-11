package day11

import day06.main
import grouped
import readInput

data class Monkey(
    var inspections: Long = 0L,
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val test: Long,
    val nextMonkeyTrue: Int,
    val nextMonkeyFalse: Int
) {
    fun doBusiness(monkeys: List<Monkey>, relief: (Long) -> Long) {
        inspections += items.size
        items.forEach { item ->
            val finalWorry = relief(operation(item))
            val nextMonkey = if (finalWorry % test == 0L) nextMonkeyTrue else nextMonkeyFalse
            monkeys[nextMonkey].items.add(finalWorry)
        }
        items.clear()
    }

}

fun main() {
    fun parseArgument(input: Long, operand: String) = when {
        operand == "old" -> input
        operand.toIntOrNull() != null -> operand.toLong()
        else -> throw UnsupportedOperationException(operand)
    }

    fun parse(input: List<String>) = input.map { it.ifEmpty { null } }.grouped().map { lines ->
        Monkey(
            items = lines[1].substringAfter("Starting items: ").split("""\D+""".toRegex()).map(String::toLong)
                .toMutableList(),
            operation = lines[2].substringAfter("Operation: new = ").let {
                val ops = it.split(" ")
                when (ops[1]) {
                    "*" -> { worry -> parseArgument(worry, ops[0]) * parseArgument(worry, ops[2]) }
                    "+" -> { worry -> parseArgument(worry, ops[0]) + parseArgument(worry, ops[2]) }
                    else -> throw UnsupportedOperationException()
                }
            },
            test = lines[3].substringAfter("Test: divisible by ").toLong(),
            nextMonkeyTrue = lines[4].substringAfter("If true: throw to monkey ").toInt(),
            nextMonkeyFalse = lines[5].substringAfter("If false: throw to monkey ").toInt()
        )
    }

    fun List<Monkey>.keepAway(rounds: Int, relief: (Long) -> Long): Long {
        repeat(rounds) {
            forEach { monkey -> monkey.doBusiness(this, relief) }
        }
        return map(Monkey::inspections).sortedDescending().take(2).reduce(Long::times)
    }

    fun part1(monkeys: List<Monkey>) = monkeys.keepAway(20) { worry -> (worry / 3.0).toLong() }

    fun part2(monkeys: List<Monkey>): Long {
        val mcd = monkeys.map(Monkey::test).reduce(Long::times)
        return monkeys.keepAway(10000) { worry -> worry % mcd }
    }


    val input = readInput(::main.javaClass.packageName)
    println("Part1=\n" + part1(parse(input)))
    println("Part2=\n" + part2(parse(input)))
}
