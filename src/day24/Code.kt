package day24

import Coord
import closeNeighbours
import day06.main
import readInput
import java.util.*
import kotlin.math.abs

fun main() {
    data class Step(val coord: Coord, val time: Int) {
        fun distanceTo(point: Pair<Int, Int>) = abs(point.first - coord.first) + abs(point.second - coord.second)
        fun estimate(point: Pair<Int, Int>) = time + distanceTo(point)
    }

    data class Maze(val map: List<Pair<Coord, Char>>) {
        val walls = map.filter { it.second == '#' }.map { it.first }.toSet()
        val maxX = walls.maxOf { it.first }
        val maxY = walls.maxOf { it.second }
        val start = Coord(first = 0, second = -1)
        val end = Coord(first = maxX - 1, second = maxY)
        val horizontalBlizzards = (0 until maxX).associateWith { time ->
            (0..maxY).flatMap { row ->
                map.filter { it.second == '>' || it.second == '<' }.groupBy { it.first.second }
                    .mapValues { (_, blizzards) ->
                        blizzards.map { (c, direction) ->
                            if (direction == '>') {
                                c.copy(first = (c.first + time) % maxX)
                            } else {
                                c.copy(first = ((c.first - time) % maxX + maxX) % maxX)
                            }
                        }
                    }[row].orEmpty()
            }.toSet()
        }
        val verticalBlizzards = (0 until maxY).associateWith { time ->
            (0 until maxX).flatMap {
                map.filter { it.second == '^' || it.second == 'v' }.groupBy { it.first.first }
                    .mapValues { (_, blizzards) ->
                        blizzards.map { (c, direction) ->
                            if (direction == 'v') {
                                c.copy(second = (c.second + time) % maxY)
                            } else {
                                c.copy(second = ((c.second - time) % maxY + maxY) % maxY)
                            }
                        }
                    }[it].orEmpty()
            }.toSet()
        }

        fun isValid(nextStep: Step) = nextStep.coord !in walls
                && nextStep.coord !in horizontalBlizzards[nextStep.time % maxX]!!
                && nextStep.coord !in verticalBlizzards[nextStep.time % maxY]!!
                && nextStep.coord.first in 0 until maxX && nextStep.coord.second in 0 until maxY

        fun distance(from: Coord = start, destination: Coord = end, time: Int = 0): Int {
            val queue = PriorityQueue<Step>(compareBy { it.distanceTo(destination) })
            queue.add(Step(from, time))
            var minTime = Int.MAX_VALUE
            val seen = mutableSetOf<Step>()
            while (queue.isNotEmpty()) {
                val step = queue.remove()!!
                if (step in seen || step.time > minTime || step.estimate(destination) >= minTime) continue
                seen.add(step)
                if (step.coord == destination) {
                    minTime = minOf(minTime, step.time)
                } else {
                    queue.addAll((step.coord.closeNeighbours + step.coord).map {
                        Step(it, step.time + 1)
                    }.filter {
                        it.coord == destination || it.coord == from || (it !in seen && this.isValid(it))
                    })
                }
            }
            return minTime
        }

    }

    fun parse(input: List<String>) = Maze(input.flatMapIndexed { y, s ->
        s.mapIndexedNotNull { x, c -> (Coord(x - 1, y - 1) to c).takeIf { it.second != '.' } }
    })


    fun part1(input: Maze) = input.distance()

    fun part2(input: Maze) = with(input) {
        distance(
            from = input.start,
            destination = input.end,
            time = distance(
                from = input.end,
                destination = input.start,
                time = distance(
                    from = input.start,
                    destination = input.end
                )
            )
        )
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}

