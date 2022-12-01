fun main() {
    fun part1(input: List<String>) = input.map(String::toIntOrNull).grouped().maxOfOrNull {
        val carriedCalories = it.sum()
        carriedCalories
    }

    fun part2(input: List<String>) = input
        .map(String::toIntOrNull)
        .grouped()
        .map(List<Int>::sum) //calories for each elf
        .sortedDescending()
        .take(3)
        .sum()

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
