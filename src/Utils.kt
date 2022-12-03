import java.io.File
import java.lang.management.ManagementFactory
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun isDebug() = ManagementFactory.getRuntimeMXBean().inputArguments.any { "jdwp=" in it }
fun readInput(dir: String, test: Boolean = isDebug()) = File(File("src", dir), if (test) "test.txt" else "input.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
@Suppress("unused")
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * Groups a list of T separated by nulls
 */
fun <T> List<T?>.grouped(): List<List<T>> = fold(mutableListOf(mutableListOf<T>())) { groups, item ->
    groups.apply {
        if (item != null) {
            last().add(item)
        } else {
            add(mutableListOf())
        }
    }
}

typealias Coord = Pair<Int, Int>

val Coord.neighbours: List<Coord>
    get() = listOf(
        Coord(first - 1, second - 1),
        Coord(first, second - 1),
        Coord(first + 1, second - 1),
        Coord(first - 1, second),
        Coord(first + 1, second),
        Coord(first - 1, second + 1),
        Coord(first, second + 1),
        Coord(first + 1, second + 1)
    )

val Coord.closeNeighbours: List<Coord>
    get() = listOf(
        Coord(first, second - 1),
        Coord(first - 1, second),
        Coord(first + 1, second),
        Coord(first, second + 1)
    )

fun Long.pow(exponent: Long): Long = (0 until exponent).fold(1L) { a, _ -> a * this }