fun main() {
	val dirs = mapOf(
		'>' to Pair(1, 0),
		'<' to Pair(-1, 0),
		'v' to Pair(0, 1),
		'^' to Pair(0, -1),
	)

	data class Position(var x: Int, var y: Int)

	fun MutableList<MutableList<Char>>.execute(move: Char, pos: Position): Position {
		val (dx, dy) = dirs[move]!!

		fun findFreeSpace(pos: Position): Position {
			val current = Position(pos.x, pos.y)
			var currentChar = this[current.y][current.x]
			while (currentChar != '.') {
				current.x += dx
				current.y += dy
				currentChar = this[current.y][current.x]
				when (currentChar) {
					'#' -> { return pos }
				}

			}
			return current
		}

		fun walkBackAndMoveTo(target: Position): Position {
			val current = Position(target.x, target.y)
			var currentChar = this[target.y][target.x]
			while (currentChar != '@') {
				this[current.y][current.x] = this[current.y - dy][current.x - dx]
				current.x -= dx
				current.y -= dy
				currentChar = this[current.y][current.x]
			}
			this[current.y][current.x] = '.'
			return Position(current.x + dx, current.y + dy)
		}

		val freeSpacePosition = findFreeSpace(pos)
		if (freeSpacePosition == pos) {
			return pos
		}

		return walkBackAndMoveTo(freeSpacePosition)
	}

	fun MutableList<MutableList<Char>>.calcGPS(): Long {
		var sum = 0L
		this.forEachIndexed { y, line ->
			line.forEachIndexed { x, c ->
				if (c == 'O') {
					sum += 100 * y + x
				}
			}
		}
		return sum
	}


	fun parseInput(input: List<String>): Pair<MutableList<MutableList<Char>>, String> {
		val grid = mutableListOf<MutableList<Char>>()
		input.takeWhile { it.isNotBlank() }.forEach { line ->
			val tmp = mutableListOf<Char>()
			for (c in line) {
				tmp.add(c)
			}
			grid.add(tmp)
		}
		val moves = input.dropWhile { it.isNotBlank() }.drop(1).joinToString("")

		return Pair(grid, moves)
	}

	fun getStartingPosition(grid: MutableList<MutableList<Char>>): Position {
		grid.forEachIndexed { y, line ->
			val x = line.indexOf('@')
			if (x != -1) return Position(x, y)
		}
		return Position(-1, -1)
	}

	fun part1(input: List<String>): Long {
		val (grid, moves) = parseInput(input)
		var pos = getStartingPosition(grid)

		for (move in moves) {
			pos = grid.execute(move, pos)
		}

		return grid.calcGPS()
	}


	fun MutableList<MutableList<Char>>.expand(): MutableList<MutableList<Char>> {
		val expanded = mutableListOf<MutableList<Char>>()
		this.forEach { line ->
			val tmp = mutableListOf<Char>()
			line.forEach { c ->
				when (c) {
					'#' -> {
						tmp.add('#')
						tmp.add('#')
					}
					'O' -> {
						tmp.add('[')
						tmp.add(']')
					}
					'.' -> {
						tmp.add('.')
						tmp.add('.')
					}
					'@' -> {
						tmp.add('@')
						tmp.add('.')
					}
				}
			}
			expanded.add(tmp)
		}

		return expanded
	}


	fun part2(input: List<String>): Int {
		val (grid, moves) = parseInput(input)
		val expandedGrid = grid.expand()
		expandedGrid.forEach { it.joinToString("").println() }

		var pos = getStartingPosition(expandedGrid)

		pos = expandedGrid.execute('<', pos)
		pos = expandedGrid.execute('<', pos)
		pos = expandedGrid.execute('<', pos)
		pos = expandedGrid.execute('<', pos)
		pos = expandedGrid.execute('<', pos)
		pos = expandedGrid.execute('<', pos)
		pos = expandedGrid.execute('v', pos)
		expandedGrid.forEach { it.joinToString("").println() }
		for (move in moves) {
//			pos = grid.execute(move, pos)
		}

		return -1
	}

	val testInput = readInput("Day15_test")
	val input = readInput("Day15")

	check(part1(readInput("Day15_test_small")) == 2028L)
	check(part1(testInput) == 10092L)
	part1(input).println()

	part2(readInput("Day15_test_small2")).println()
//	check(part2(testInput) == 9021)
//	part2(input).println()
}