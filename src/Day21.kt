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
		fun getNeighbours(): List<Pos> {
			val neighbours = mutableListOf<Pos>()

			for (dir in dirs) {
				val (dx, dy) = dir.value
				val next = Pos(x + dx, y + dy, path + dir.key)
				if (next.y in numpad.indices && next.x in numpad[0].indices && numpad[next.y][next.x] != 'X') {
					neighbours.add(next)
				}
			}
			if (path.isNotEmpty()) {
				neighbours.sortBy { it.path.last() != path.last() }
			}
			return neighbours
		}

		override fun equals(other: Any?): Boolean {
			if (other !is Pos) return false
			return (this.x == other.x && this.y == other.y)
		}

		override fun hashCode(): Int {
			var result = x
			result = 31 * result + y
			return result
		}
	}

	fun numpadCodeToDirections(code: String): String {
		fun bfs(c: Char, start: Pos): Pos {
			val q = mutableListOf<Pos>()
			q.add(start)
			val visited = mutableSetOf<Pos>()

			while (q.isNotEmpty()) {
				val current = q.removeFirst()

				if (numpad[current.y][current.x] == c) {
					return current
				}
				if (current in visited) continue

				val neighbours = current.getNeighbours()
				for (n in neighbours) {
					q.add(n)
				}

			}
			return Pos(0,0,"")
		}

		var start = Pos(2, 3, "")
		return buildString {
			for (c in code) {
				val res = bfs(c, start)
				append(res.path + "A")
				start = Pos(res.x, res.y, "")
			}
		}.toString()
	}

	fun part1(input: List<String>): Int {
		var sum = 0
		for (line in input) {
			numpadCodeToDirections(line).println()
		}

		return sum
	}

	fun part2(input: List<String>): Int {

		return -1
	}

	val testInput = readInput("Day${dayNr}_test")
	val input = readInput("Day${dayNr}")

	part1(testInput).println()
//	check(part1(testInput) == 126384)
//	part1(input).println()
//
//	check(part2(testInput) == -1)
//	part2(input).println()
}
