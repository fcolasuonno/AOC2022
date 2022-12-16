package day16

import day02.cartesian
import day06.main
import readInput

fun main() {
    val format = """Valve ([A-Z]+) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)""".toRegex()

    data class Valve(val id: String, val flow: Int, val conn: List<String>)

    fun parse(input: List<String>) = input.map {
        format.matchEntire(it)!!.destructured.let { (valve, flow, conn) ->
            Valve(
                valve,
                flow.toInt(),
                conn.split(", ")
            )
        }
    }.associateBy { it.id }

    fun movementCostMap(input: Map<String, Valve>, movementCost: Int = 1) = buildMap<Pair<String, String>, Int> {
        input.values.forEach { from ->
            from.conn.forEach { to ->
                this[from.id to to] = movementCost
            }
        }
        val explore = input.keys.cartesian().toMutableSet()
        explore.removeIf { it.first == it.second }
        while (explore.isNotEmpty()) {
            entries.forEach { (tunnel, currentCost) ->
                val (from, to) = tunnel
                input.getValue(to).conn.forEach {
                    val existing = this[(from to it)]
                    val costToReach = currentCost + movementCost
                    if (existing == null || existing > costToReach) {
                        this[(from to it)] = costToReach
                    }
                }

            }
            explore.removeAll(this.keys)
        }
    }


    fun part1(input: Map<String, Valve>) = movementCostMap(input).let { cost ->

        val actionableValves = input.values.filter { it.flow > 0 }.map { it.id }
        val activationCost = actionableValves.let {
            it.cartesian().filter { it.first != it.second }.associateWith { cost[it]!! + 1 }
                .plus(it.associate { ("AA" to it) to cost["AA" to it]!! + 1 })
        }

        data class EscapePlan(
            val valveToCheck: List<String> = actionableValves,
            val current: String = "AA",
            val releasedPressure: Int = 0,
            val time: Int = 0
        ) : Iterable<EscapePlan> {

            override fun iterator() = iterator {
                yield(this@EscapePlan)
                valveToCheck.takeIf { it.isNotEmpty() }?.map { valve ->
                    val act = activationCost[current to valve]!!
                    val newTime = time + act
                    copy(
                        valveToCheck = valveToCheck - valve,
                        current = valve,
                        releasedPressure = releasedPressure + (30 - newTime) * input[valve]!!.flow,
                        time = newTime
                    )
                }?.filter { it.time < 31 }?.forEach { yieldAll(it) }
            }

        }
        EscapePlan().asSequence().filter { it.time < 30 }.map { it.releasedPressure }.max()
    }

    fun part2(input: Map<String, Valve>): Any {
        val actionableValves = input.values.filter { it.flow > 0 }.map { it.id }.toSet()
        val cost = movementCostMap(input)
        val activationCost = actionableValves.let {
            it.cartesian().filter { it.first != it.second }.associateWith { cost[it]!! + 1 }
                .plus(it.associate { ("AA" to it) to cost["AA" to it]!! + 1 })
        }

        val minMap = mutableMapOf<Set<String>, Int>().withDefault { Int.MAX_VALUE }

        data class NewEscapePlan(
            val valveToCheck: Set<String> = actionableValves,
            val current: String = "AA",
            val elephantCurrent: String = "AA",
            val releasedPressure: Int = 0,
            val time: Int = 0,
            val elephantTime: Int = 0
        ) : Iterable<NewEscapePlan> {

            override fun iterator() = iterator {

                yield(this@NewEscapePlan)

                valveToCheck.takeIf { it.isNotEmpty() }?.flatMap { valve ->
                    val newValveToCheck = valveToCheck - valve
                    listOfNotNull(
                        copy(
                            valveToCheck = newValveToCheck,
                            current = valve,
                            elephantCurrent = elephantCurrent,
                            releasedPressure = releasedPressure + (26 - (time + activationCost[current to valve]!!)) * input[valve]!!.flow,
                            time = time + activationCost[current to valve]!!,
                            elephantTime = elephantTime
                        ).takeIf { maxOf(it.time, it.elephantTime) <= minMap.getValue(newValveToCheck) }
                            ?.also { minMap[newValveToCheck] = maxOf(it.time, it.elephantTime) },
                        copy(
                            valveToCheck = newValveToCheck,
                            current = current,
                            elephantCurrent = valve,
                            releasedPressure = releasedPressure + (26 - (elephantTime + activationCost[elephantCurrent to valve]!!)) * input[valve]!!.flow,
                            time = time,
                            elephantTime = elephantTime + activationCost[elephantCurrent to valve]!!
                        ).takeIf { maxOf(it.time, it.elephantTime) <= minMap.getValue(newValveToCheck) }
                            ?.also { minMap[newValveToCheck] = maxOf(it.time, it.elephantTime) }
                    )
                }?.filter { it.time < 26 && it.elephantTime < 26 }?.sortedByDescending { it.releasedPressure }
                    ?.forEach { yieldAll(it) }
            }

        }
        return NewEscapePlan().asSequence().map { it.releasedPressure }.max()

    }


    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}
