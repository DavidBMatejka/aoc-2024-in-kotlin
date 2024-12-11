private val map = mutableMapOf<Pair<String, Int>, Long>()
fun main() {
	fun applyRules(stone: String, blinksRemaining: Int): Long {
		if (blinksRemaining == 0) return 1
		if (Pair(stone, blinksRemaining) in map) return map[Pair(stone, blinksRemaining)]!!

		if (stone == "0") {
			return applyRules("1", blinksRemaining - 1)
		}

		if (stone.length % 2 == 0) {
			val left = stone.substring(0,stone.length / 2)
			var right = stone.substring(stone.length / 2)
			right = right.dropWhile { it == '0' }
			if (right.isBlank()) right = "0"

			val leftErg = applyRules(left, blinksRemaining - 1)
			val rightErg = applyRules(right, blinksRemaining - 1)
			val sum = leftErg + rightErg
			map[Pair(stone, blinksRemaining)] = sum
			return sum
		}

		return applyRules("${stone.toLong()*2024}", blinksRemaining - 1)
	}

	fun part1(input: List<String>): Long {
		var sum = 0L

		for (stone in input[0].split(" ")) {
			sum += applyRules(stone, 25)
		}
		return sum
	}

	fun part2(input: List<String>): Long {
		var sum = 0L

		for (stone in input[0].split(" ")) {
			sum += applyRules(stone, 75)
		}
		return sum
	}

	val testInput = readInput("Day11_test")
	val input = readInput("Day11")

	check(part1(testInput) == 55312L)
	part1(input).println()

//	check(part2(testInput) == )
	part2(input).println()
}