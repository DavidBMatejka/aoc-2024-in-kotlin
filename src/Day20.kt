import kotlin.math.abs

fun main() {
	val dayNr = 20

	fun transpose(grid: MutableList<String>): MutableList<String> {
		val transposed = MutableList(grid.size) { "" }
		grid.forEach {
			for (j in 0..<grid.size) {
				transposed[j] = transposed[j] + it[j]
			}
		}
		return transposed
	}

	fun parseInput(input: List<String>): MutableList<CharArray> {
		val map = mutableListOf<CharArray>()
		for (line in input) {
			map.add(line.toCharArray())
		}
		return map
	}

	data class Shift(val dx: Int, val dy: Int)

	val dirs = listOf(
		Shift(1, 0),
		Shift(-1, 0),
		Shift(0, 1),
		Shift(0, -1),
	)

	data class Position(val x: Int, val y: Int) {
		override fun toString(): String {
			return "$x,$y"
		}

		var steps = 0L
		fun getNeighbours(map: List<CharArray>): List<Position> {
			val neighbours = mutableListOf<Position>()
			for (dir in dirs) {
				val (dx, dy) = dir
				val next = Position(x + dx, y + dy)
				if (next.x in map[0].indices && next.y in map.indices
					&& map[next.y][next.x] != '#'
				) {
					next.steps = steps + 1
					neighbours.add(next)
				}
			}
			return neighbours
		}


	}

	fun bfs(map: MutableList<CharArray>): MutableMap<Position, Long> {
		var start = Position(0, 0)
		var end = Position(0, 0)
		map.forEachIndexed { y, line ->
			val xS = line.indexOf('S')
			val xE = line.indexOf('E')
			if (xS != -1) start = Position(xS, y)
			if (xE != -1) end = Position(xE, y)
		}

		val q = mutableListOf<Position>()
		q.add(start)
		val posToCost = mutableMapOf<Position, Long>()
		posToCost[start] = 0

		while (q.isNotEmpty()) {
			val current = q.removeFirst()

			if (current.x == end.x && current.y == end.y) {
				posToCost[current] = current.steps
				return posToCost
			}

			val neighbours = current.getNeighbours(map)
			neighbours.forEach { next ->
				if (next !in posToCost) {
					posToCost[next] = next.steps
					q.add(next)
				}
			}

		}

		return mutableMapOf()
	}

	fun part1(input: List<String>): Int {
		val map = parseInput(input)
		val posToCost = bfs(map)

		val shortcuts = mutableListOf<Long>()
		input.forEachIndexed { y, line ->
			line.windowed(3).forEachIndexed { i, it ->
				if (it == ".#." || it == "S#." || it == ".#S" || it == "E#." || it == ".#E") {
					val start = posToCost[Position(i, y)]!!
					val end = posToCost[Position(i + 2, y)]!!
					// always needs to take two steps for the shortcut so -2
					val shortcutCost = abs(end - start) - 2
					shortcuts.add(shortcutCost)
				}
			}
		}

		val transposed = transpose(input.toMutableList())
		transposed.forEachIndexed { y, line ->
			line.windowed(3).forEachIndexed { i, it ->
				if (it == ".#." || it == "S#." || it == ".#S" || it == "E#." || it == ".#E") {
					// neet to flip x and y since map is transposed
					val start = posToCost[Position(y, i)]!!
					val end = posToCost[Position(y, i + 2)]!!
					// always needs to take two steps for the shortcut so -2
					val shortcutCost = abs(end - start) - 2
					shortcuts.add(shortcutCost)
				}
			}
		}

		return shortcuts.filter {
			it >= 100
		}.size
	}

	fun calcDistance(a: Position, b: Position): Int {
		val tmp = abs(a.x - b.x) + abs(a.y - b.y)
		if (tmp <= 20) return tmp

		return Int.MAX_VALUE
	}

	fun part2(input: List<String>): Int {
		val map = parseInput(input)
		val posToCost = bfs(map)

		val timeSaves = mutableMapOf<String, Long>()

		posToCost.forEach { a ->
			// todo recalculating b to a which should be removed
			posToCost.forEach { b ->
				if (a != b && ("$a|$b" !in timeSaves || "$b|$a" !in timeSaves)) {
					val d = calcDistance(a.key, b.key)
					if (posToCost[a.key]!! + d < posToCost[b.key]!!) {
						val distanceSave = abs(posToCost[b.key]!! - (posToCost[a.key]!! + d))
						timeSaves["$a|$b"] = distanceSave
					}
				}
			}
		}

//		prints to check if example case checks
//		timeSaves
//			.filter { it.value >= 50 }.values
//			.groupBy { it }
//			.forEach {
//				println("${it.value.size} cheat that save ${it.key} picos")
//			}

		return timeSaves.filter {
			it.value >= 100
		}.size
	}

	val testInput = readInput("Day${dayNr}_test")
	val input = readInput("Day${dayNr}")

	part1(testInput)
	part1(input).println()

	part2(testInput)
	part2(input).println()
}
