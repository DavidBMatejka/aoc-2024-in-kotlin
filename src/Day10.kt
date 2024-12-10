private data class Pos(val x: Int, val y: Int, var currentValue: Int) {
	fun getNeighbours(input: List<String>): List<Pos> {
		return buildList {
			for (dir in dirs) {
				val (dx, dy) = dir
				val next = Pos(x + dx, y + dy, -1)
				if (next.x in input[0].indices && next.y in input.indices
					&& input[next.y][next.x].digitToInt() == currentValue + 1
				) {
					next.currentValue = input[next.y][next.x].digitToInt()
					add(next)
				}
			}
		}
	}

	fun bfs(input: List<String>): Pair<Int, Int> {
		val endPoints = mutableSetOf<Pos>()
		var distinctPaths = 0
		val q = mutableListOf(this)

		while (q.isNotEmpty()) {
			val current = q.removeFirst()

			current.getNeighbours(input).forEach { next ->
				if (next.currentValue == 9) {
					endPoints.add(next)
					distinctPaths++
				} else {
					q.add(next)
				}
			}
		}
		return Pair(endPoints.size, distinctPaths)
	}
}

private val dirs = listOf(
	Pair(-1, 0),
	Pair(1, 0),
	Pair(0, -1),
	Pair(0, 1),
)

fun main() {
	fun parseInput(input: List<String>): MutableList<Pos> {
		val trailheads = mutableListOf<Pos>()
		input.forEachIndexed { y, line ->
			line.forEachIndexed { x, c ->
				if (c.digitToInt() == 0) {
					trailheads.add(Pos(x, y, 0))
				}
			}
		}
		return trailheads
	}

	fun part1(input: List<String>): Int {
		val trailheads = parseInput(input)
		var sum = 0
		trailheads.forEach { trailhead ->
			sum += trailhead.bfs(input).first
		}
		return sum
	}

	fun part2(input: List<String>): Int {
		val trailheads = parseInput(input)

		var sum = 0
		trailheads.forEach { trailhead ->
			sum += trailhead.bfs(input).second
		}
		return sum
	}

	val testInput = readInput("Day10_test")
	val input = readInput("Day10")

	check(part1(testInput) == 36)
	part1(input).println()

	check(part2(testInput) == 81)
	part2(input).println()
}