package day21

import day06.main
import readInput
import java.math.BigInteger

sealed class Monkey
data class Number(val number: BigInteger) : Monkey()
data class Op(val left: String, val right: String, val op: (BigInteger, BigInteger) -> BigInteger) : Monkey()

fun main() {

    fun parse(input: List<String>) = input.map {
        val (name, value) = it.split(": ")
        name to (value.toLongOrNull()?.let { Number(BigInteger.valueOf(it)) } ?: value.split(" ")
            .let { (left, op, right) ->
                Op(
                    left, right, when (op) {
                        "+" -> BigInteger::plus
                        "-" -> BigInteger::minus
                        "*" -> BigInteger::times
                        "/" -> BigInteger::div
                        else -> throw UnsupportedOperationException()
                    }
                )
            })
    }.toMap()

    fun Map<String, Monkey>.value(node: String) =
        DeepRecursiveFunction<Monkey, BigInteger> { monkey ->
            when (monkey) {
                is Number -> monkey.number
                is Op -> monkey.op(
                    callRecursive(getValue(monkey.left)),
                    callRecursive(getValue(monkey.right))
                )
            }
        }(getValue(node))

    fun part1(input: Map<String, Monkey>) = input.value("root")

    fun part2(input: MutableMap<String, Monkey>) = with(input) {
        val (left, right) = getValue("root") as Op
        input["humn"] = Number(BigInteger.ZERO)
        val zero = value(left) - value(right)
        input["humn"] = Number(BigInteger.valueOf(Long.MAX_VALUE))
        val max = value(left) - value(right)
        val crescent = max > zero
        generateSequence(0L..Long.MAX_VALUE) { range ->
            val mid = range.first + (range.last - range.first) / 2L
            input["humn"] = Number(BigInteger.valueOf(mid))
            val diff = value(left) - value(right)
            when (diff.signum().let {
                if (crescent) it else -it
            }) {
                0 -> null
                1 -> range.first..mid
                else -> mid..range.last
            }
        }.last()
        input.value("humn").toLong()
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input.toMutableMap()))
}
