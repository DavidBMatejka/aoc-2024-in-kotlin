import java.util.PriorityQueue

fun main() {
	data class Move(val dx: Int, val dy: Int)

	val dirs = mapOf(
		0 to Move(1, 0),
		1 to Move(0, 1),
		2 to Move(-1, 0),
		3 to Move(0, -1),
	)

	data class Pos(val x: Int, val y: Int, val dir: Int) {
		var cost = 0
		var prev: Pos? = null

		fun getNeighbours(grid: List<String>): List<Pos> {
			val neighbours = mutableListOf<Pos>()

			val (dx, dy) = dirs[dir]!!
			val next = Pos(x + dx, y + dy, dir)
			next.cost = cost + 1

			if (grid[next.y][next.x] != '#') {
				neighbours.add(next)
			}

			val rotateLeft = Pos(x, y, Math.floorMod(dir - 1, 4))
			val rotateRight = Pos(x, y, Math.floorMod(dir + 1, 4))
			rotateRight.cost = cost + 1000
			rotateLeft.cost = cost + 1000
			neighbours.add(rotateRight)
			neighbours.add(rotateLeft)

			return neighbours
		}
	}

	data class Path(val pos: Pos, val cost: Int)

	fun bfs(grid: List<String>, start: Pos): List<Path> {
		val q = PriorityQueue<Pos>(compareBy { it.cost })
		q.add(start)

		val costSoFar = mutableMapOf<Pos, Int>().withDefault { Int.MAX_VALUE }
		costSoFar[start] = 0

		val possiblePaths = mutableListOf<Path>()

		while (q.isNotEmpty()) {
			val current = q.poll()

			if (grid[current.y][current.x] == 'E') {
				possiblePaths.add(Path(current, current.cost))
			}

			for (neighbour in current.getNeighbours(grid)) {
				if (neighbour !in costSoFar || neighbour.cost <= costSoFar[neighbour]!!) {
					neighbour.prev = current
					costSoFar[neighbour] = neighbour.cost
					q.add(neighbour)
				}
			}
		}

		return possiblePaths
	}

	fun parseInput(input: List<String>): Pair<List<String>, Pos> {
		var start = Pos(0, 0, 0)
		val grid = buildList {
			input.forEachIndexed { y, line ->
				line.forEachIndexed { x, c ->
					if (c == 'S') {
						start = Pos(x, y, 0)
					}
				}
				add(line)
			}
		}
		return Pair(grid, start)
	}

	fun part1(input: List<String>): Int {
		val (grid, start) = parseInput(input)
		return bfs(grid, start).minBy { it.cost }.cost
	}

	fun part2(input: List<String>): Int {
		val (grid, start) = parseInput(input)
		val paths = bfs(grid, start)

		val filteredByMin = paths.filter{
			it.cost == paths.minBy { it.cost }.cost
		}

		val set = mutableSetOf<Pos>()
		for (path in filteredByMin) {
			var current: Pos? = path.pos
			while (current != null) {
				set.add(Pos(current.x, current.y, 0))
				current = current.prev
			}
		}

		return set.size
	}

	val input = readInput("Day16")

	check(part1(readInput("Day16_test")) == 7036)
	check(part1(readInput("Day16_test2")) == 11048)
	part1(input).println()

	check(part2(readInput("Day16_test")) == 45)
	check(part2(readInput("Day16_test2")) == 64)
	part2(input).println()
}
