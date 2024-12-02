import kotlin.math.absoluteValue

fun main() {
    fun descIsSafe(numbers: List<Int>): Boolean {
        for (i in 0..numbers.size - 2) {
            val cur = numbers[i]
            val next = numbers[i + 1]
            if (cur < next) return false
            if ((cur - next).absoluteValue > 3 || (cur - next) == 0) return false
        }
        return true
    }
    fun ascIsSafe(numbers: List<Int>): Boolean {
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
            if(ascIsSafe(numbers)) {
                sum++
            } else if(descIsSafe(numbers)) {
                sum++
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val numbers = line.split(" ").map {it.toInt()}.toMutableList()
            if(ascIsSafe(numbers)) {
                sum++
                continue
            }
            if(descIsSafe(numbers)) {
                sum++
                continue
            }

            for (i in numbers.indices) {
                val oneRemoved = numbers.toMutableList()
                oneRemoved.removeAt(i)
                if(ascIsSafe(oneRemoved)) {
                    sum++
                    break
                }
                if(descIsSafe(oneRemoved)) {
                    sum++
                    break
                }
            }
        }
        return sum
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
