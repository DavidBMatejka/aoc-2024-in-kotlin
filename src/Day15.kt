fun main() {
	val dirs = mapOf(
		'>' to Pair(1, 0),
		'<' to Pair(-1, 0),
		'v' to Pair(0, 1),
		'^' to Pair(0, -1),
	)

	data class Position(var x: Int, var y: Int)
	data class GridElement(val x: Int, val y: Int, val char: Char)

	fun MutableList<MutableList<Char>>.execute(
		move: Char,
		pos: Position,
		inPart2: Boolean = false,
	): Position {
		val (dx, dy) = dirs[move]!!

		fun findFreeSpace(pos: Position): Position {
			val current = Position(pos.x, pos.y)
			var currentChar = this[current.y][current.x]
			while (currentChar != '.') {
				current.x += dx
				current.y += dy
				currentChar = this[current.y][current.x]
				when (currentChar) {
					'#' -> {
						return pos
					}
				}

			}
			return current
		}

		fun walkBackAndMoveTo(target: Position): Position {
			val current = Position(target.x, target.y)
			var currentChar = this[target.y][target.x]
			while (currentChar != '@') {
				this[current.y][current.x] =
					this[current.y - dy][current.x - dx]
				current.x -= dx
				current.y -= dy
				currentChar = this[current.y][current.x]
			}
			this[current.y][current.x] = '.'
			return Position(current.x + dx, current.y + dy)
		}

		fun getElementsToPushVertical(pos: Position): Set<GridElement> {
			val gridElements = mutableSetOf<GridElement>()

			val currentChar = this[pos.y][pos.x]
			when (currentChar) {
				'@' -> {
					gridElements.add(GridElement(pos.x, pos.y, '@'))
				}
				'#' -> {
					return setOf(GridElement(-1, -1, '#'))
				}
				'.' -> {
					return emptySet()
				}
				'[' -> {
					gridElements.add(GridElement(pos.x, pos.y, '['))
					gridElements.add(GridElement(pos.x + 1, pos.y, ']'))
					gridElements.addAll(getElementsToPushVertical(Position(pos.x + 1, pos.y + dy)))
				}
				']' -> {
					gridElements.add(GridElement(pos.x, pos.y, ']'))
					gridElements.add(GridElement(pos.x - 1, pos.y, '['))
					gridElements.addAll(getElementsToPushVertical(Position(pos.x - 1, pos.y + dy)))
				}
			}
			gridElements.addAll(getElementsToPushVertical(Position(pos.x, pos.y + dy)))

			return gridElements
		}

		fun getElementsToPushHorizontal(pos: Position): Set<GridElement> {
			val gridElements = mutableSetOf<GridElement>()

			val currentChar = this[pos.y][pos.x]
			when (currentChar) {
				'#' -> {
					return setOf(GridElement(-1, -1, '#'))
				}
				'.' -> {
					return emptySet()
				}
				else -> {
					gridElements.add(GridElement(pos.x, pos.y, this[pos.y][pos.x]))
					gridElements.addAll(getElementsToPushHorizontal(Position(pos.x + dx, pos.y)))
				}
			}

			return gridElements
		}

		if (inPart2) {
			if (move == '^' || move == 'v') {
				val elementsToPush = getElementsToPushVertical(pos)
				var newPos = pos
				if (elementsToPush.none { it.char == '#' }) {
					elementsToPush.forEach {
						this[it.y][it.x] = '.'
					}
					elementsToPush.forEach {
						if (it.char == '@') {
							newPos = Position(it.x, it.y + dy)
						}
						this[it.y + dy][it.x] = it.char
					}
				}
				return newPos
			} else if (move == '>' || move == '<') {
				val elementsToPush = getElementsToPushHorizontal(pos)
				var newPos = pos
				if (elementsToPush.none { it.char == '#' }) {
					elementsToPush.forEach {
						this[it.y][it.x] = '.'
					}
					elementsToPush.forEach {
						if (it.char == '@') {
							newPos = Position(it.x + dx, it.y)
						}
						this[it.y][it.x + dx] = it.char
					}
				}
				return newPos
			}
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
				if (c == 'O' || c == '[') {
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


	fun part2(input: List<String>): Long {
		val (grid, moves) = parseInput(input)
		val expandedGrid = grid.expand()

		var pos = getStartingPosition(expandedGrid)
		for (move in moves) {
			pos = expandedGrid.execute(move, pos, inPart2 = true)
		}

		return expandedGrid.calcGPS()
	}

	val testInput = readInput("Day15_test")
	val input = readInput("Day15")

	check(part1(readInput("Day15_test_small")) == 2028L)
	check(part1(testInput) == 10092L)
	part1(input).println()

	check(part2(testInput) == 9021L)
	part2(input).println()
}