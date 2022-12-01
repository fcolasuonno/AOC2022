fun main() {
    fun part1(input: List<String>) =
        mutableListOf(mutableListOf<Int>()).apply {
            input.forEach {
                if (it.isEmpty()) {
                    add(mutableListOf())
                } else {
                    last().add(it.toInt())
                }
            }
        }.maxOfOrNull(List<Int>::sum)

    fun part2(input: List<String>) =
        mutableListOf(mutableListOf<Int>()).apply {
            input.forEach {
                if (it.isEmpty()) {
                    add(mutableListOf())
                } else {
                    last().add(it.toInt())
                }
            }
        }.map(List<Int>::sum).sortedDescending().take(3).sum()

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
