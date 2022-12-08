package day08

import Coord
import day06.main
import readInput

typealias IntGrid = Array<Array<Int>>

class Forest(private val trees: IntGrid) : Iterable<Coord> {
    val maxX = trees[0].size
    val maxY = trees.size
    fun isVisible(x: Int, y: Int) = (0 until x).all { trees[it][y] < trees[x][y] }
            || (0 until y).all { trees[x][it] < trees[x][y] }
            || ((x + 1) until maxX).all { trees[it][y] < trees[x][y] }
            || ((y + 1) until maxY).all { trees[x][it] < trees[x][y] }

    fun scenicScore(x: Int, y: Int): Int {
        val left = ((x - 1) downTo 1).takeWhile { trees[it][y] < trees[x][y] }.size
        val top = ((y - 1) downTo 1).takeWhile { trees[x][it] < trees[x][y] }.size
        val right = ((x + 1) until (maxX - 1)).takeWhile { trees[it][y] < trees[x][y] }.size
        val down = ((y + 1) until (maxY - 1)).takeWhile { trees[x][it] < trees[x][y] }.size
        return (left + 1) * (top + 1) * (right + 1) * (down + 1)
    }

    override fun iterator() = iterator {
        (1 until (maxX - 1)).forEach { x ->
            (1 until (maxY - 1)).forEach { y ->
                yield(Coord(x, y))
            }
        }
    }
}

fun main() {
    fun parse(input: List<String>) = Forest(Array(input.first().length) { x ->
        Array(input.size) { y ->
            input[x][y].digitToInt()
        }
    })

    fun part1(input: Forest) = input.count { (x, y) ->
        input.isVisible(x, y)
    } + (input.maxX - 1 + input.maxY - 1) * 2 //edges

    fun part2(input: Forest) = input.maxOf { (x, y) ->
        input.scenicScore(x, y)
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
