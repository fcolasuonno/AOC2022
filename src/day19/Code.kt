package day19

import day06.main
import readInput

fun main() {

    val ORE = 0
    val CLAY = 1
    val OBSIDIAN = 2
    val GEODE = 3

    data class Blueprint(
        val time: Int = 24,
        val resources: List<Int> = List(4) { 0 },
        val robots: List<Int> = List(4) { if (it == ORE) 1 else 0 },
        val costs: List<Int>
    ) {
        private val oreRobotOreCost: Int = costs[0]
        private val clayRobotOreCost: Int = costs[1]
        private val obsidianRobotOreCost: Int = costs[2]
        private val obsidianRobotClayCost: Int = costs[3]
        private val geodeRobotOreCost: Int = costs[4]
        private val geodeRobotObsidianCost: Int = costs[5]
        private val maxOreCost = maxOf(oreRobotOreCost, clayRobotOreCost, obsidianRobotOreCost, geodeRobotOreCost)

        fun maxGeodes(): Int {
            val maxGeodeRobots = minOf(
                (resources[ORE] + (0..time).sumOf { robots[ORE] + it }) / geodeRobotOreCost + 1,
                (resources[OBSIDIAN] + (0..time).sumOf { robots[OBSIDIAN] + it }) / geodeRobotObsidianCost + 1,
                time
            )
            return resources[GEODE] + (1..maxGeodeRobots).sumOf { robots[GEODE] + it } - 1
        }

        fun increaseRobots(): List<Blueprint> = buildList {
            if (resources[ORE] >= geodeRobotOreCost && resources[OBSIDIAN] >= geodeRobotObsidianCost) {
                // When possible to build a geodeRobot always do it (not sure why we can exclude all the other options)
                add(copy(time = time - 1, resources = resources.mapIndexed { index, i ->
                    when (index) {
                        ORE -> i - geodeRobotOreCost
                        OBSIDIAN -> i - geodeRobotObsidianCost
                        else -> i
                    }
                }.zip(robots, kotlin.Int::plus), robots = robots.mapIndexed { index, i ->
                    when (index) {
                        GEODE -> i + 1
                        else -> i
                    }
                }))
            } else {
                add(copy(time = time - 1, resources = resources.zip(robots, kotlin.Int::plus)))
                if (resources[ORE] >= oreRobotOreCost && robots[ORE] < maxOreCost) {
                    add(copy(time = time - 1, resources = resources.mapIndexed { index, i ->
                        when (index) {
                            ORE -> i - oreRobotOreCost
                            else -> i
                        }
                    }.zip(robots, kotlin.Int::plus), robots = robots.mapIndexed { index, i ->
                        when (index) {
                            ORE -> i + 1
                            else -> i
                        }
                    }))
                }
                if (resources[ORE] >= clayRobotOreCost && robots[CLAY] < obsidianRobotClayCost) {
                    add(copy(time = time - 1, resources = resources.mapIndexed { index, i ->
                        when (index) {
                            ORE -> i - clayRobotOreCost
                            else -> i
                        }
                    }.zip(robots, kotlin.Int::plus), robots = robots.mapIndexed { index, i ->
                        when (index) {
                            CLAY -> i + 1
                            else -> i
                        }
                    }))
                }
                if (resources[ORE] >= obsidianRobotOreCost && resources[CLAY] >= obsidianRobotClayCost && robots[OBSIDIAN] < geodeRobotObsidianCost) {
                    add(copy(time = time - 1, resources = resources.mapIndexed { index, i ->
                        when (index) {
                            ORE -> i - obsidianRobotOreCost
                            CLAY -> i - obsidianRobotClayCost
                            else -> i
                        }
                    }.zip(robots, kotlin.Int::plus), robots = robots.mapIndexed { index, i ->
                        when (index) {
                            OBSIDIAN -> i + 1
                            else -> i
                        }
                    }))
                }
            }
        }

    }

    fun parse(input: List<String>) = input.map { blueprint ->
        blueprint.split("""\D+""".toRegex()).mapNotNull { it.toIntOrNull() }.let {
            Blueprint(costs = it.drop(1))
        }
    }

    fun part1(input: List<Blueprint>) = input.mapIndexed { index, blueprint ->
        val queue = ArrayDeque<Blueprint>()
        queue.add(blueprint)
        var maxGeodes = 0

        while (queue.isNotEmpty()) {
            val current = queue.removeLast()
            maxGeodes = maxOf(maxGeodes, current.resources[GEODE])

            if (current.time != 0 && current.maxGeodes() > maxGeodes) {
                queue.addAll(current.increaseRobots())
            }
        }
        (index + 1) * maxGeodes
    }.sum()

    fun part2(input: List<Blueprint>) = input.map { it.copy(time = 32) }.take(3).map { blueprint ->
        val queue = ArrayDeque<Blueprint>()
        queue.add(blueprint)
        var maxGeodes = 0

        while (queue.isNotEmpty()) {
            val current = queue.removeLast()
            maxGeodes = maxOf(maxGeodes, current.resources[GEODE])

            if (current.time != 0 && current.maxGeodes() > maxGeodes) {
                queue.addAll(current.increaseRobots())
            }
        }
        maxGeodes
    }.reduce(Int::times)

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}