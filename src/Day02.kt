import kotlin.math.absoluteValue

fun main() {
    fun isDscSafe(numbers: List<Int>): Boolean {
        for (i in 0..numbers.size - 2) {
            val cur = numbers[i]
            val next = numbers[i + 1]
            if (cur < next) return false
            if ((cur - next).absoluteValue > 3 || (cur - next) == 0) return false
        }
        return true
    }
    fun isAscSafe(numbers: List<Int>): Boolean {
        for (i in 0..numbers.size - 2) {
            val cur = numbers[i]
            val next = numbers[i + 1]
            if (cur > next) return false
            if ((cur - next).absoluteValue > 3 || (cur - next) == 0) return false
        }
        return true
    }
    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val numbers = line.split(" ").map {it.toInt()}
            val first = numbers[0]
            val second = numbers[1]
            if (first < second) {
                if(isAscSafe(numbers)) {
                    sum++
                }
            } else if(isDscSafe(numbers)) {
                sum++
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val numbers = line.split(" ").map {it.toInt()}.toMutableList()
            if(isAscSafe(numbers)) {
                sum++
                continue
            }
            if(isDscSafe(numbers)) {
                sum++
                continue
            }

            var foundSafety = false
            for (i in numbers.indices) {
                val oneRemoved = numbers.toMutableList()
                oneRemoved.removeAt(i)
                if(isAscSafe(oneRemoved)) {
                    sum++
                    foundSafety = true
                    break
                }
                if(isDscSafe(oneRemoved)) {
                    sum++
                    foundSafety = true
                    break
                }
            }
        }
        return sum
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
