fun main() {
	fun checkIfCorrect(
		rules: MutableList<List<String>>,
		update: List<String>,
	): Triple<Boolean, Int, Int> {
		for (rule in rules) {
			val left = update.indexOf(rule[0])
			val right = update.indexOf(rule[1])
			if (left == -1 || right == -1) {
				continue
			}
			if (left > right) {
				return Triple(false, left, right)
			}
		}
		return Triple(true, -1, -1)
	}


	fun part1(input: List<String>): Int {
		val blocks =
			input.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
				if (line.isBlank()) {
					acc.add(mutableListOf())
				} else {
					acc.last().add(line)
				}
				acc
			}

		val rules = blocks[0].map { it.split("|") }.toMutableList()

		return blocks[1].fold(0) { sum, page ->
			val splitted = page.split(",")
			if (checkIfCorrect(rules, splitted).first) {
				val middle = splitted[splitted.size / 2]
				return@fold sum + middle.toInt()
			}
			sum
		}
	}

	fun part2(input: List<String>): Int {
		val blocks =
			input.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
				if (line.isBlank()) {
					acc.add(mutableListOf())
				} else {
					acc.last().add(line)
				}
				acc
			}

		val rules = blocks[0].map { it.split("|") }.toMutableList()

		return blocks[1].fold(0) { sum, page ->
			val splitted = page.split(",").toMutableList()

			var reviewedPage = checkIfCorrect(rules, splitted)
			while (reviewedPage.first == false) {
				reviewedPage = checkIfCorrect(rules, splitted)
				val (correct, left, right) = reviewedPage
				
				if (correct) {
					val middle = splitted[splitted.size / 2]
					return@fold sum + middle.toInt()
				}
				
				val tmp = splitted[left]
				splitted[left] = splitted[right]
				splitted[right] = tmp
			}
			sum
		}
	}

	val testInput = readInput("Day05_test")
	check(part1(testInput) == 143)
	check(part2(testInput) == 123)

	val input = readInput("Day05")
	part1(input).println()
	part2(input).println()
}