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
		val list =
			input.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
				if (line.isBlank()) {
					acc.add(mutableListOf())
				} else {
					acc.last().add(line)
				}
				acc
			}

		val firstBlock = list[0]
		val rules = mutableListOf<List<String>>()
		for (line in firstBlock) {
			rules.add(line.split("|"))
		}
		var sum = 0
		for (update in list[1]) {
			val splitted = update.split(",")
			if (checkIfCorrect(rules, splitted).first) {
				val middle = splitted[splitted.size / 2]
				sum += middle.toInt()
			}
		}
		return sum
	}

	fun part2(input: List<String>): Int {
		val list =
			input.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
				if (line.isBlank()) {
					acc.add(mutableListOf())
				} else {
					acc.last().add(line)
				}
				acc
			}

		val firstBlock = list[0]
		val rules = mutableListOf<List<String>>()
		for (line in firstBlock) {
			rules.add(line.split("|"))
		}
		var sum = 0
		for (update in list[1]) {
			val splitted = update.split(",").toMutableList()

			if (checkIfCorrect(rules, splitted).first == true) {
				continue
			}

			while (true) {
				val (correct, left, right) = checkIfCorrect(rules, splitted)
				if (!correct) {
					val tmp = splitted[left]
					splitted[left] = splitted[right]
					splitted[right] = tmp

				} else {
					val middle = splitted[splitted.size / 2]
					sum += middle.toInt()
					break
				}
			}
		}
		return sum
	}

	val testInput = readInput("Day05_test")
	check(part1(testInput) == 143)
	check(part2(testInput) == 123)

	val input = readInput("Day05")
	part1(input).println()
	part2(input).println()
}