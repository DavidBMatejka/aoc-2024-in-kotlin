import kotlin.math.min

fun main() {
	val dayNr = 19

	fun parseInput(input: List<String>): Pair<List<String>, List<String>> {
		val rules = input[0].split(", ")
		val designs = input.dropWhile { it.isNotEmpty() }.drop(1)
		return Pair(rules, designs)
	}

	fun part1(input: List<String>): Int {
		val (rules, designs) = parseInput(input)
		val maxLength = rules.maxBy { it.length }.length

		val mem = mutableMapOf<String, Boolean>()
		fun checkDesign(design: String): Boolean {
			if (design == "") return true
			if (design in mem) return mem.getOrElse(design) { false }

			for (i in 0..min(maxLength, design.length)) {
				val start = design.substring(0, i)
				val rest = design.substring(i)

				if (start !in rules) continue

				if (checkDesign(rest)) {
					mem[design] = true
					return true
				}
			}

			mem[design] = false
			return false
		}

		var sum = 0
		for (design in designs) {
			if (checkDesign(design)) {
				sum++
			}
		}
		return sum
	}


	fun part2(input: List<String>): Long {
		val (rules, designs) = parseInput(input)
		val maxLength = rules.maxBy { it.length }.length

		val mem = mutableMapOf<String, Long>()
		fun countWays(pattern: String, rules: List<String>): Long {
			if (pattern == "") return 1L
			if (pattern in mem) return mem[pattern]!!

			var sum = 0L
			for (i in 0..min(maxLength, pattern.length)) {
				val start = pattern.substring(0, i)
				val end = pattern.substring(i)

				if (start !in rules) continue

				sum += countWays(end, rules)
			}

			mem[pattern] = sum
			return sum
		}

		var sum = 0L
		for (pattern in designs) {
			sum += countWays(pattern, rules)
		}

		return sum
	}

	val testInput = readInput("Day${dayNr}_test")
	val input = readInput("Day${dayNr}")

	check(part1(testInput) == 6)
	part1(input).println()

	part2(testInput)

	check(part2(testInput) == 16L)
	part2(input).println()
}
