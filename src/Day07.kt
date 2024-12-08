fun main() {
	fun calc (target: Long, current: Long, rest: List<Long>, extra: Boolean): Long {
		if (current > target) return 0
		if (current == target && rest.isEmpty()) return target
		if (rest.isEmpty()) return 0

		val next = rest[0]

		var tmp = calc(target, current * next, rest.drop(1), extra)
		if (tmp == target) return target

		if (extra) {
			tmp = calc(target, "$current$next".toLong(), rest.drop(1), extra)
			if (tmp == target) return target
		}

		tmp = calc(target, current + next, rest.drop(1), extra)
		if (tmp == target) return target


		return 0
	}
	fun part1(input: List<String>): Long {
		var sum = 0L
		for (line in input) {
			val splitted = line.split(": ", " ").map { it.toLong() }
			val target = splitted[0]
			val rest = splitted.drop(1)
			sum += calc(target, 0, rest, false)
		}
		return sum
	}

	fun part2(input: List<String>): Long {
		var sum = 0L
		for (line in input) {
			val splitted = line.split(": ", " ").map { it.toLong() }
			val target = splitted[0]
			val rest = splitted.drop(1)
			sum += calc(target, 0, rest, true)
		}
		return sum
	}

	val testInput = readInput("Day07_test")
	check(part1(testInput) == 3749L)
	check(part2(testInput) == 11387L)

	val input = readInput("Day07")
	part1(input).println()
	part2(input).println()
}
