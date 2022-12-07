package day07

import readInput

sealed class Day06Node {
    abstract val size: Int
}

private class File(override val size: Int) : Day06Node()
private class Dir(
    val fileContents: MutableMap<String, File> = mutableMapOf(),
    val dirContents: MutableMap<String, Dir> = mutableMapOf(),
    val parent: Dir? = null
) : Day06Node(), Iterable<Dir> {
    override val size: Int by lazy {
        fileContents.values.sumOf { it.size } +
                dirContents.values.sumOf { it.size }
    }

    override fun iterator() = iterator {
        yield(this@Dir)
        dirContents.values.forEach { yieldAll(it) }
    }
}

fun main() {
    fun parse(input: List<String>) = Dir().apply { ->
        input.filter { it != "$ ls" } //ls is useLS
            .drop(1) //already created root node
            .map { it.split(" ") }
            .fold(this) { cwd, command ->
                if (command[0] == "$" && command[1] == "cd") {
                    val newDir = command[2]
                    requireNotNull(if (newDir == "..") cwd.parent else cwd.dirContents[newDir]) //new cwd
                } else {
                    cwd.apply {
                        val name = command[1]
                        if (command[0] == "dir") {
                            dirContents[name] = Dir(parent = cwd)
                        } else {
                            fileContents[name] = File(command[0].toInt())
                        }
                    }
                }
            }
    }

    fun part1(input: Dir) = input.map { it.size }.filter { it < 100000 }.sumOf { it }

    fun part2(input: Dir): Int {
        val total = 70000000
        val free = total - input.size
        val required = 30000000 - free
        return input.map { it.size }.filter { it >= required }.minOf { it }
    }

    val input = parse(readInput(::main.javaClass.packageName))
    println("Part1=\n" + part1(input))
    println("Part2=\n" + part2(input))
}

