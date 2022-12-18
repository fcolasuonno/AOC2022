package day18

import day06.main
import readInput

fun main() {
    data class Cube(val x: Int, val y: Int, val z: Int) {
        fun dimension(i: Int) = when (i % 3) {
            0 -> x
            1 -> y
            else -> z
        }

        fun adjacent() = setOf(
            copy(x = x - 1),
            copy(x = x + 1),
            copy(y = y - 1),
            copy(y = y + 1),
            copy(z = z - 1),
            copy(z = z + 1),
        )
    }

    fun parse(input: List<String>) =
        input.map { it.split(",").map(String::toInt).let { (x, y, z) -> Cube(x, y, z) } }.toSet()

    fun Cube.extractOtherDimension(dimension: Int) = Pair(dimension(dimension + 1), dimension(dimension + 2))

    fun Set<Cube>.adjacentFaces(dimension: Int) =
        groupBy { it.dimension(dimension) }.let { List(it.keys.max() + 1) { i -> it[i] ?: emptyList() } }.zipWithNext()
            .sumOf { (prev, next) ->
                prev.map { cube -> cube.extractOtherDimension(dimension) }
                    .intersect(next.map { cube -> cube.extractOtherDimension(dimension) }.toSet()).count()
            }

    fun part1(cubes: Set<Cube>) =
        6 * cubes.size - 2 * (cubes.adjacentFaces(0) + cubes.adjacentFaces(1) + cubes.adjacentFaces(2))

    fun part2(input: Set<Cube>) = with(mutableSetOf<Cube>()) {
        val xRange = (input.minOf { it.x } - 1)..(input.maxOf { it.x } + 1)
        val yRange = (input.minOf { it.y } - 1)..(input.maxOf { it.y } + 1)
        val zRange = (input.minOf { it.z } - 1)..(input.maxOf { it.z } + 1)
        generateSequence(
            Cube(xRange.first, yRange.first, zRange.first).adjacent()
        ) { frontier ->
            frontier.filter { cube ->
                cube.x in xRange && cube.y in yRange && cube.z in zRange && cube !in this && cube !in input
            }.takeIf(List<Cube>::isNotEmpty)?.let { validCubes ->
                if (addAll(validCubes)) {
                    validCubes.flatMap { it.adjacent() }.toSet()
                } else null
            }
        }.last().let {
            sumOf { external -> external.adjacent().count { it in input } }
        }
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
