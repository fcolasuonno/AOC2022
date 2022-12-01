import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(dir: String, name: String) = File(File("src", dir), "$name.txt")
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
fun <T> List<T?>.grouped(): List<List<T>> = mutableListOf(mutableListOf<T>()).apply {
    this@grouped.forEach {
        if (it != null) {
            last().add(it)
        } else {
            add(mutableListOf())
        }
    }
}
