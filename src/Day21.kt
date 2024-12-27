fun main() {
	val dayNr = 21

	val dirs = mapOf(
		'>' to Pair(1, 0),
		'^' to Pair(0, -1),
		'v' to Pair(0, 1),
		'<' to Pair(-1, 0),
	)

	val numpad = listOf(
		"789",
		"456",
		"123",
		"X0A"
	)

	val dirpad = listOf(
		"X^A",
		"<v>"
	)

	data class Pos(val x: Int, val y: Int, val path: String) {
		fun getNeighbours(pad: List<String>): List<Pos> {
			val neighbours = mutableListOf<Pos>()

			for (dir in dirs) {
				val (dx, dy) = dir.value
				val next = Pos(x + dx, y + dy, path + dir.key)
				if (next.y in pad.indices && next.x in pad[0].indices && pad[next.y][next.x] != 'X') {
					neighbours.add(next)
				}
			}
			return neighbours
		}

		override fun toString(): String {
			return "($x, $y)"
		}
	}

	fun bfs(start: Pos, end: Pos, pad: List<String>): List<String> {
		if (start == end) return listOf("A")

		val paths = mutableListOf<String>()
		var shortestPathLength = Int.MAX_VALUE

		val q = mutableListOf(start)

		while (q.isNotEmpty()) {
			val current = q.removeFirst()
			if (current.path.length > shortestPathLength) continue

			if (current.x == end.x && current.y == end.y) {
				paths.add(current.path + "A")
				shortestPathLength = current.path.length
			}

			for (neighbour in current.getNeighbours(pad)) {
				q.add(neighbour)
			}
		}
		return paths
	}

	fun generatePaths(pad: List<String>): Map<Pair<Char, Char>, List<String>> {
		val mapping = mutableMapOf<Pair<Char, Char>, List<String>>()

		for (y1 in pad.indices) {
			for (x1 in pad[0].indices) {
				val start = Pos(x1, y1, "")
				val startChar = pad[y1][x1]
				if (startChar == 'X') continue

				for (y2 in pad.indices) {
					for (x2 in pad[0].indices) {
						val endChar = pad[y2][x2]
						if (endChar == 'X') continue
						val end = Pos(x2, y2, "")
						val paths = bfs(start, end, pad)
						mapping[Pair(startChar, endChar)] = paths
					}
				}
			}
		}

		return mapping
	}

	val allNumpadPaths = generatePaths(numpad)
	val allDirPaths = generatePaths(dirpad)

	// checked todd ginsbergs solution for some help with the recursive function
	// https://todd.ginsberg.com/post/advent-of-code/2024/day21/
	fun calcCost(
		code: String,
		depth: Int,
		transitions: Map<Pair<Char, Char>, List<String>> = allDirPaths,
		mem: MutableMap<Pair<String, Int>, Long> = mutableMapOf(),
	): Long {
		return mem.getOrPut(Pair(code, depth)) {
			"A$code".zipWithNext().sumOf {
				val paths: List<String> = transitions.getValue(it)
				if (depth == 0) {
					paths.minOf { it.length }.toLong()
				} else {
					paths.minOf { path ->
						calcCost(path, depth - 1, mem = mem)
					}
				}
			}
		}
	}

	fun part1(input: List<String>): Long {
		return input.sumOf { line ->
			calcCost(line, 2, allNumpadPaths) * line.dropLast(1).toLong()
		}
	}

	fun part2(input: List<String>): Long {
		return input.sumOf { line ->
			calcCost(line, 25, allNumpadPaths) * line.dropLast(1).toLong()
		}
	}

	val testInput = readInput("Day${dayNr}_test")
	val input = readInput("Day${dayNr}")

	check(part1(testInput) == 126384L)
	part1(input).println()
	part2(input).println()
}
