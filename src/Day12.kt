fun main() {
	val dirs = listOf(
		Pair(-1, 0),
		Pair(1, 0),
		Pair(0, 1),
		Pair(0, -1),
	)

	data class FencePos(val x: Float, val y: Float) {
		override fun toString(): String {
			return "($x, $y)"
		}
	}

	data class Plot(val x: Int, val y: Int, val letter: Char) {
		fun getNeighbours(grid: List<String>): List<Plot> {
			val neighbours = mutableListOf<Plot>()

			for (dir in dirs) {
				val (dx, dy) = dir
				if ((x + dx) in grid[0].indices && (y + dy) in grid.indices
					&& grid[y + dy][x + dx] == letter
				) {
					neighbours.add(Plot(x + dx, y + dy, letter))
				}
			}

			return neighbours
		}

		fun getFenceDirs(grid: List<String>): Set<Pair<Int, Int>> {
			val fenceDirs = mutableSetOf<Pair<Int, Int>>()

			for (dir in dirs) {
				val (dx, dy) = dir
				if ((x + dx) !in grid[0].indices || (y + dy) !in grid.indices) {
					fenceDirs.add(dir)
				} else if (grid[y + dy][x + dx] != letter) {
					fenceDirs.add(dir)
				}
			}

			return fenceDirs
		}
	}


	fun bfs(
		grid: List<String>,
		plot: Plot,
		plotVisited: MutableSet<Plot>,
	): Pair<Int, Int> {
		val visited = mutableSetOf<Plot>()
		val q = mutableListOf<Plot>()
		q.add(plot)

		var fenceCost = 0
		while (q.isNotEmpty()) {
			val current = q.removeFirst()
			if (current in visited) {
				continue
			}

			visited.add(current)
			val neighbours = current.getNeighbours(grid)
			fenceCost += 4 - neighbours.size
			for (neighbour in neighbours) {
				q.add(neighbour)
			}
		}

		plotVisited.addAll(visited)
		return Pair(fenceCost, visited.size)
	}

	fun part1(input: List<String>): Int {
		val plotVisited = mutableSetOf<Plot>()
		var sum = 0
		input.forEachIndexed { i, line ->
			line.forEachIndexed { j, c ->
				val plot = Plot(j, i, c)
				if (plot !in plotVisited) {
					val (area, fence) = bfs(input, plot, plotVisited)
					sum += area * fence
				}
			}
		}
		return sum
	}

	fun bfs2(
		grid: List<String>,
		plot: Plot,
		plotVisited: MutableSet<Plot>,
	): Long {
		val visited = mutableSetOf<Plot>()
		val fence = mutableSetOf<FencePos>()
		var fenceCost = 0L

		val q = mutableListOf<Plot>()
		q.add(plot)

		while (q.isNotEmpty()) {
			val current = q.removeLast()

			if (current in visited) {
				continue
			}
			visited.add(current)

			val neighbours = current.getNeighbours(grid)
			for (neighbour in neighbours) {
				q.add(neighbour)
			}
			for (dir in current.getFenceDirs(grid)) {
				val (dx, dy) = dir
				val fencePos =
					FencePos(
						current.x + (dx / 4f),
						current.y + (dy / 4f)
					)
				fence.add(fencePos)
			}
		}

		fence.toSortedSet(compareBy({ it.x }, { it.y }))
			.groupBy { it.y }
			.filter { !it.key.toString().endsWith(".0") }
			.values.forEach {
				fenceCost += it.zipWithNext().count {
					it.first.x + 1 != it.second.x
				} + 1
			}

		fence.toSortedSet(compareBy({ it.x }, { it.y }))
			.groupBy { it.x }
			.filter { !it.key.toString().endsWith(".0") }
			.values.forEach {
				fenceCost += it.zipWithNext().count {
					it.first.y + 1 != it.second.y
				} + 1
			}


		plotVisited.addAll(visited)
		val sum = fenceCost * visited.size.toLong()
//		"area for letter ${plot.letter}: ${visited.size}, fenceCost: $fenceCost, fenceSize: ${fence.size}, sum: $sum".println()
		return sum
	}

	fun part2(input: List<String>): Long {
		val plotVisited = mutableSetOf<Plot>()
		var sum = 0L
		input.forEachIndexed { i, line ->
			line.forEachIndexed { j, c ->
				val plot = Plot(j, i, c)
				if (plot !in plotVisited) {
					sum += bfs2(input, plot, plotVisited)
				}
			}
		}
		return sum
	}

	val testInput = readInput("Day12_test")
	val input = readInput("Day12")

	check(part1(testInput) == 1930)
	part1(input).println()

	check(part2(readInput("Day12_test2")) == 436L)
	check(part2(readInput("Day12_test80")) == 80L)
	check(part2(readInput("Day12_test368")) == 368L)
	check(part2(readInput("Day12_test")) == 1206L)
	part2(input).println()
}
