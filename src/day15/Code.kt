package day15

import Coord
import day06.main
import readInput
import kotlin.math.abs

fun main() {
    fun Coord.distanceTo(b: Coord) = abs(first - b.first) + abs(second - b.second)

    val format = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

    fun parse(input: List<String>) = input.map {
        format.matchEntire(it)!!.destructured.toList().map(String::toInt).let { (sx, sy, bx, by) ->
            val sensor = Coord(sx, sy)
            val beacon = Coord(bx, by)
            Pair(sensor, sensor.distanceTo(beacon))
        }
    }

    fun Coord.rangeCoveredAtRow(y: Int, range: Int): IntRange? {
        val reducedDistance = range - abs(second - y)
        return reducedDistance.takeIf { it > 0 }?.let { (first - it)..(first + it) }
    }

    fun List<IntRange>.merged() = buildList<IntRange> {
        this@merged.sortedWith(compareBy({ it.first }, { it.last })).forEach { newRange ->
            add(lastOrNull { it.last >= newRange.first }?.let {
                removeLast()
                it.first..maxOf(it.last, newRange.last)
            } ?: newRange)
        }
    }


    fun part1(input: List<Pair<Coord, Int>>, y: Int = 2000000) = input.mapNotNull { (s, d) ->
        s.rangeCoveredAtRow(y = y, range = d)
    }.merged().sumOf { it.last - it.first }

    fun part2(input: List<Pair<Coord, Int>>, space: Int = 4000000) = (0..space).asSequence().map { y ->
        input.mapNotNull { (s, d) ->
            s.rangeCoveredAtRow(y = y, range = d)?.let { maxOf(0, it.first)..minOf(space, it.last) }
        }.merged()
    }.withIndex().first { it.value.size == 2 }.let { (y, ranges) -> ranges.first().last + 1 to y }.let { (x, y) ->
        x * 4000000L + y
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
