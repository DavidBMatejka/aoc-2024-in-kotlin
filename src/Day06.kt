fun main() {
	val directionMap = mapOf(
		0 to Pair(0, -1),
		1 to Pair(1, 0),
		2 to Pair(0, 1),
		3 to Pair(-1, 0),
	)

	data class Guard(var x: Int, var y: Int, var direction: Int) {
		fun turn() {
			direction++
			if (direction == 4) direction = 0
		}

		fun move(map: MutableList<MutableList<Char>>) {
			var (dx, dy) = directionMap[direction]!!
			if (map[dy + y][dx + x] == '#') {
				turn()
				return
			}
			x += dx
			y += dy
		}

		fun insideMap(map: MutableList<MutableList<Char>>): Boolean {
			val (dx, dy) = directionMap[direction]!!
			if (x + dx >= map[0].size) return false
			if (y + dy >= map.size) return false
			if (x + dx < 0) return false
			if (y + dy < 0) return false

			return true
		}
	}

	fun part1(input: List<String>): Pair<Int, MutableSet<Pair<Int, Int>>> {
		val map = mutableListOf<MutableList<Char>>()

		var guard = Guard(0, 0, -1)
		input.forEachIndexed { y, line ->
			map.add(line.toMutableList())
			val x = line.indexOf("^")
			if (x != -1) {
				guard = Guard(x, y, 0)
			}
		}

		val visited = mutableSetOf(Pair(guard.x, guard.y))
		while (guard.insideMap(map)) {
			guard.move(map)
			visited.add(Pair(guard.x, guard.y))
		}

		return Pair(visited.size, visited)
	}

	fun getMap(input: List<String>): MutableList<MutableList<Char>> {
		val map = mutableListOf<MutableList<Char>>()
		input.forEachIndexed { y, line ->
			map.add(line.toMutableList())
		}

		return map
	}

	fun part2(input: List<String>, visited: MutableSet<Pair<Int, Int>>): Int {
		var start = Pair(-1, -1)
		input.forEachIndexed { y, line ->
			val x = line.indexOf("^")
			if (x != -1) {
				start = Pair(x, y)
			}
		}
		visited.remove(start)

		var loopCount = 0
		for (pos in visited) {
			val copy = getMap(input)
			copy[pos.second][pos.first] = '#'
			val guard = Guard(start.first, start.second, 0)

			val potentialLoop = mutableSetOf(Pair(guard.x, guard.y))
			var alreadyVisited = 0
			while (guard.insideMap(copy)) {
				guard.move(copy)
				val currentPos = Pair(guard.x, guard.y)
				if (currentPos in potentialLoop) {
					alreadyVisited++
				} else {
					alreadyVisited = 0
				}
				if (alreadyVisited > 200) {
					loopCount++
					break
				}
				potentialLoop.add(currentPos)
			}
		}

		return loopCount
	}

	val testInput = readInput("Day06_test")
	val (testErg, testVisited) = part1(testInput)
	check(testErg == 41)
	check(part2(testInput, testVisited) == 6)

	val input = readInput("Day06")
	val (erg, visited) = part1(input)
	erg.println()
	part2(input, visited).println()
}