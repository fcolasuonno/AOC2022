package day22

import Coord
import day06.main
import readInput

fun main() {
    abstract class Tile {
        abstract val nextStep: MutableList<Coord>
        abstract val nextDir: MutableList<Int>
    }

    data class Step(
        override val nextStep: MutableList<Coord> = mutableListOf(),
        override val nextDir: MutableList<Int> = mutableListOf()
    ) : Tile()

    data class Wall(
        override val nextStep: MutableList<Coord> = mutableListOf(),
        override val nextDir: MutableList<Int> = mutableListOf()
    ) : Tile()

    data class Position(val coord: Coord, val dir: Int = 0) {
        fun password() = 4 * (coord.first + 1) + 1000 * (coord.second + 1) + dir
    }

    fun parseMap(input: List<String>) = input.flatMapIndexed { y, s ->
        s.mapIndexedNotNull { x, type ->
            when (type) {
                '.' -> Coord(x, y) to Step()
                '#' -> Coord(x, y) to Wall()
                else -> null
            }
        }
    }.toMap()

    fun parseInstructions(instructions: String) = instructions.run {
        split("""\d+""".toRegex()).zip(split("""\D+""".toRegex())) { dir, amount ->
            when (dir) {
                "R" -> 1
                "L" -> -1
                else -> 0
            } to amount.toInt()
        }
    }

    fun parse(input: List<String>) = parseMap(input.takeWhile { it.isNotEmpty() }) to parseInstructions(input.last())

    fun Map<Coord, Tile>.wrapPart1() {
        forEach { (c, tile) ->
            val next = c.copy(first = c.first + 1)
            tile.nextStep.add(when (this[next]) {
                is Step -> next
                is Wall -> c
                else -> keys.filter {
                    it.second == c.second
                }.minBy { it.first }.takeIf { this[it]!! is Step } ?: c
            })
            tile.nextDir.add(0)
        }
        forEach { (c, tile) ->
            val next = c.copy(second = c.second + 1)
            tile.nextStep.add(when (this[next]) {
                is Step -> next
                is Wall -> c
                else -> keys.filter {
                    it.first == c.first
                }.minBy { it.second }.takeIf { this[it]!! is Step } ?: c
            })
            tile.nextDir.add(0)
        }
        forEach { (c, tile) ->
            val next = c.copy(first = c.first - 1)
            tile.nextStep.add(when (this[next]) {
                is Step -> next
                is Wall -> c
                else -> keys.filter {
                    it.second == c.second
                }.maxBy { it.first }.takeIf { this[it]!! is Step } ?: c
            })
            tile.nextDir.add(0)
        }
        forEach { (c, tile) ->
            val next = c.copy(second = c.second - 1)
            tile.nextStep.add(when (this[next]) {
                is Step -> next
                is Wall -> c
                else -> keys.filter {
                    it.first == c.first
                }.maxBy { it.second }.takeIf { this[it]!! is Step } ?: c
            })
            tile.nextDir.add(0)
        }
    }

    fun part1(input: List<String>) = parse(input).let { (map, instructions) ->
        map.apply { wrapPart1() }
        instructions.fold(Position(map.keys.minWith(compareBy(Coord::second, Coord::first)))) { position, instr ->
            val (turn, walk) = instr
            val newDir = (position.dir + turn + 4) % 4
            var newCoord = position.coord
            repeat(walk) {
                newCoord = map[newCoord]!!.nextStep[newDir]
            }
            position.copy(coord = newCoord, dir = newDir)
        }.password()
    }

    fun Map<Coord, Tile>.wrapPart2(
        right: (Coord) -> Coord,
        left: (Coord) -> Coord,
        up: (Coord) -> Coord,
        down: (Coord) -> Coord,
        rightDir: (Coord) -> Int,
        leftDir: (Coord) -> Int,
        upDir: (Coord) -> Int,
        downDir: (Coord) -> Int,
    ) {
        forEach { (c, tile) ->
            val next = c.copy(first = c.first + 1)
            tile.nextStep.add(when (this[next]) {
                is Step -> next
                is Wall -> c
                else -> right(c).takeIf { this[it]!! is Step } ?: c
            })
            tile.nextDir.add(
                if (this[next] == null && this[right(c)]!! is Step) rightDir(c)
                else 0
            )
        }
        forEach { (c, tile) ->
            val next = c.copy(second = c.second + 1)
            tile.nextStep.add(when (this[next]) {
                is Step -> next
                is Wall -> c
                else -> down(c).takeIf { this[it]!! is Step } ?: c
            })
            tile.nextDir.add(
                if (this[next] == null && this[down(c)]!! is Step) downDir(c)
                else 1
            )
        }
        forEach { (c, tile) ->
            val next = c.copy(first = c.first - 1)
            tile.nextStep.add(when (this[next]) {
                is Step -> next
                is Wall -> c
                else -> left(c).takeIf { this[it]!! is Step } ?: c
            })
            tile.nextDir.add(
                if (this[next] == null && this[left(c)]!! is Step) leftDir(c)
                else 2
            )
        }
        forEach { (c, tile) ->
            val next = c.copy(second = c.second - 1)
            tile.nextStep.add(when (this[next]) {
                is Step -> next
                is Wall -> c
                else -> up(c).takeIf { this[it]!! is Step } ?: c
            })
            tile.nextDir.add(
                if (this[next] == null && this[up(c)]!! is Step) upDir(c)
                else 3
            )
        }
    }

    val SIZE = 50
    fun part2(input: List<String>) = parse(input).let { (map, instructions) ->
        map.apply {
            wrapPart2(right = {
                when (it.second) {
                    in (0 * SIZE) until (1 * SIZE) -> Coord(2 * SIZE - 1, 3 * SIZE - it.second - 1)
                    in (1 * SIZE) until (2 * SIZE) -> Coord(1 * SIZE + it.second, 1 * SIZE - 1)
                    in (2 * SIZE) until (3 * SIZE) -> Coord(3 * SIZE - 1, 3 * SIZE - it.second - 1)
                    else -> Coord(1 * SIZE + (it.second - 3 * SIZE), 3 * SIZE - 1)
                }
            }, left = {
                when (it.second) {
                    in (0 * SIZE) until (1 * SIZE) -> Coord(0, 3 * SIZE - it.second - 1)
                    in (1 * SIZE) until (2 * SIZE) -> Coord(it.second - 1 * SIZE, 2 * SIZE)
                    in (2 * SIZE) until (3 * SIZE) -> Coord(1 * SIZE, 3 * SIZE - 1 - it.second)
                    else -> Coord(it.second - 2 * SIZE, 0)
                }
            }, up = {
                when (it.first) {
                    in (0 * SIZE) until (1 * SIZE) -> Coord(1 * SIZE, 1 * SIZE + it.first)
                    in (1 * SIZE) until (2 * SIZE) -> Coord(0, 2 * SIZE + it.first)
                    else -> Coord(it.first - 2 * SIZE, 4 * SIZE - 1)
                }
            }, down = {
                when (it.first) {
                    in (0 * SIZE) until (1 * SIZE) -> Coord(2 * SIZE + it.first, 0)
                    in (1 * SIZE) until (2 * SIZE) -> Coord(1 * SIZE - 1, 2 * SIZE + it.first)
                    else -> Coord(2 * SIZE - 1, it.first - 1 * SIZE)
                }
            }, rightDir = {
                when (it.second) {
                    in (0 * SIZE) until (1 * SIZE) -> 2
                    in (1 * SIZE) until (2 * SIZE) -> 3
                    in (2 * SIZE) until (3 * SIZE) -> 2
                    else -> 3
                }
            }, leftDir = {
                when (it.second) {
                    in (0 * SIZE) until (1 * SIZE) -> 0
                    in (1 * SIZE) until (2 * SIZE) -> 1
                    in (2 * SIZE) until (3 * SIZE) -> 0
                    else -> 1
                }
            }, upDir = {
                when (it.first) {
                    in (0 * SIZE) until (1 * SIZE) -> 0
                    in (1 * SIZE) until (2 * SIZE) -> 0
                    else -> 3
                }
            }, downDir = {
                when (it.first) {
                    in (0 * SIZE) until (1 * SIZE) -> 1
                    in (1 * SIZE) until (2 * SIZE) -> 2
                    else -> 2
                }
            })
        }
        instructions.fold(Position(map.keys.minWith(compareBy(Coord::second, Coord::first)))) { position, instr ->
            val (turn, walk) = instr
            var newDir = (position.dir + turn + 4) % 4
            var newCoord = position.coord
            repeat(walk) {
                val tmp = map[newCoord]!!.nextStep[newDir]
                newDir = map[newCoord]!!.nextDir[newDir]
                newCoord = tmp

            }
            Position(coord = newCoord, dir = newDir)
        }.password()
    }

    val input = readInput(::main.javaClass.packageName, false)
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
