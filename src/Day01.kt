import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        for (line in input) {
            val splitted = line.split("   ")
            left.add(splitted[0].toInt())
            right.add(splitted[1].toInt())
        }

       return left.sorted().zip(right.sorted()).fold(0) { sum, element  ->
            sum + abs(element.first - element.second)
        }
    }

    fun part2(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val rightMap = mutableMapOf<Int, Int>()
        for (line in input) {
            val splitted = line.split("   ")
            left.add(splitted[0].toInt())

            val r = splitted[1].toInt()
            rightMap[r] = rightMap.getOrDefault(r, 0) + 1
        }

        return left.fold(0) { sum, e ->
            sum + e * rightMap.getOrDefault(e, 0)
        }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
