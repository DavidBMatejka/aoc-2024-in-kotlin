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
		fun getNeighbours(grid: List<String>): List<Pos> {
			val neighbours = mutableListOf<Pos>()

			val (dx, dy) = dirs[dir]!!
			val next = Pos(x + dx, y + dy, dir)
			next.cost = cost + 1
			if (grid[next.y][next.x] != '#') {
				neighbours.add(next)
			}

			val rotateLeft = Pos( x, y, Math.floorMod(dir - 1, 4))
			val rotateRight = Pos( x, y, Math.floorMod(dir + 1, 4))
			rotateRight.cost = cost + 1000
			rotateLeft.cost = cost + 1000
			neighbours.add(rotateRight)
			neighbours.add(rotateLeft)

			return neighbours
		}
	}

	fun bfs(grid: List<String>, start: Pos): Int {
		val q = PriorityQueue<Pos>(compareBy { it.cost })
		q.add(start)

		val costSoFar = mutableMapOf<Pos, Int>().withDefault { Int.MAX_VALUE }
		costSoFar[start] = 0

		while (q.isNotEmpty()) {
			val current = q.poll()

			if (grid[current.y][current.x] == 'E') {
				return current.cost
			}

			for (neighbour in current.getNeighbours(grid)) {
				if (neighbour !in costSoFar || neighbour.cost < costSoFar[neighbour]!!) {
					costSoFar[neighbour] = neighbour.cost
					q.add(neighbour)
				}
			}
		}

		return -1
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

		val erg = bfs(grid, start)
		erg.println()
		return erg
	}

	fun part2(input: List<String>): Int {

		return -1
	}

	val testInput = readInput("Day16_test")
	val input = readInput("Day16")

	check(part1(testInput) == 7036)
	check(part1(readInput(("Day16_test2"))) == 11048)
	part1(input).println()

	check(part2(testInput) == -1)
	part2(input).println()
}
