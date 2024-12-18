fun main() {

	data class Dir(val x: Int, val y: Int)

	val dirs = listOf(
		Dir(1, 0),
		Dir(-1, 0),
		Dir(0, 1),
		Dir(0, -1),
	)

	data class Position(val x: Int, val y: Int) {
		var steps = 0

		fun getNeighbours(map: MutableList<MutableList<Char>>, currentSteps: Int): List<Position> {
			val neighbours = mutableListOf<Position>()

			for (dir in dirs) {
				if ((x + dir.x) in map.indices && (y + dir.y) in map.indices
					&& map[y + dir.y][x + dir.x] == '.'
				) {
					val next = Position(x + dir.x, y + dir.y)
					next.steps = currentSteps + 1
					neighbours.add(next)
				}
			}

			return neighbours
		}
	}

	fun bfs(map: MutableList<MutableList<Char>>): Int {
		val q = mutableListOf(Position(0, 0))
		val visited = mutableListOf<Position>()

		while (q.isNotEmpty()) {
			val current = q.removeFirst()

			if (current in visited) continue
			visited.add(current)

			if (current.x == map.size - 1 && current.y == map.size - 1) {
				return current.steps
			}

			for (neighbour in current.getNeighbours(map, current.steps)) {
				q.add(neighbour)
			}
		}

		return -1
	}

	fun parseInput(input: List<String>, size: Int, bytes: Int): MutableList<MutableList<Char>> {
		val map = MutableList(size) { MutableList(size) { '.' } }

		for (i in 0..bytes - 1) {
			val (x, y) = input[i].split(",").map { it.toInt() }
			map[y][x] = '#'
		}

		return map
	}

	fun part1(input: List<String>, size: Int, bytes: Int): Int {
		val map = parseInput(input, size, bytes)
		return bfs(map)
	}

	fun binSearch(
		input: List<String>,
		size: Int,
		low: Int,
		high: Int
	): Int {
		val middle = (high + low) / 2
		if (middle == low) return low - 1

		val map = parseInput(input, size, middle)

		var i = low
		while (i < middle) {
			val (x, y) = input[i].split(",").map { it.toInt() }
			map[y][x] = '#'
			i++
		}

		return if (bfs(map) == -1) {
			binSearch(input, size, low, middle)
		} else {
			binSearch(input, size, middle + 1, high)
		}
	}


	fun part2(input: List<String>, size: Int, bytes: Int): String {
		val index = binSearch(input, size, bytes, input.size - 1)
		return input[index]
	}

	val testInput = readInput("Day18_test")
	val input = readInput("Day18")

	check(part1(testInput, size = 7, bytes = 12) == 22)
	part1(input, size = 71, bytes = 1024).println()

	check(part2(testInput, size = 7, bytes = 12) == "6,1")
	part2(input, size = 71, bytes = 1024).println()
}
