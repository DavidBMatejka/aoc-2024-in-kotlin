import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        for (line in input) {
            val splitted = line.split("   ")
            left.add(splitted[0].trim().toInt())
            right.add(splitted[1].trim().toInt())
        }
        left.sort()
        right.sort()
        var sum = 0
        for (i in left.indices) {
            sum += abs(left[i] - right[i])
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val rightMap = mutableMapOf<Int, Int>()
        for (line in input) {
            val splitted = line.split("   ")
            left.add(splitted[0].trim().toInt())

            val r = splitted[1].trim().toInt()
            if (r !in rightMap) {
                rightMap[r] = 1
            } else {
                rightMap[r] = rightMap[r]!! + 1
            }
        }
        var sum = 0
        for (l in left) {
            if (l in rightMap) sum += l * rightMap[l]!!
        }
        return sum
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
