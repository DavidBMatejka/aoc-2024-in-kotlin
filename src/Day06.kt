fun main() {
	val directionMap = mapOf(
		0 to Pair(0, -1),
		1 to Pair(1, 0),
		2 to Pair(0, 1),
		3 to Pair(-1, 0),
	)

	data class GuardPos(var x: Int, var y: Int, var direction: Int) {
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
			return ((x + dx) in map[0].indices && (y + dy) in map.indices)
		}
	}

	fun part1(input: List<String>): Pair<Int, MutableSet<Pair<Int, Int>>> {
		val map = mutableListOf<MutableList<Char>>()

		var guardPos = GuardPos(0, 0, -1)
		input.forEachIndexed { y, line ->
			map.add(line.toMutableList())
			val x = line.indexOf("^")
			if (x != -1) {
				guardPos = GuardPos(x, y, 0)
			}
		}

		val visited = mutableSetOf(Pair(guardPos.x, guardPos.y))
		while (guardPos.insideMap(map)) {
			guardPos.move(map)
			visited.add(Pair(guardPos.x, guardPos.y))
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

	fun part2(input: List<String>): Int {
		val visited = part1(input).second
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
			val guardPos = GuardPos(start.first, start.second, 0)

			val potentialLoop = mutableSetOf(guardPos.copy())
			while (guardPos.insideMap(copy)) {
				guardPos.move(copy)
				if (guardPos in potentialLoop) {
					loopCount++
					break
				}
				potentialLoop.add(guardPos.copy())
			}
		}

		return loopCount
	}

	val testInput = readInput("Day06_test")
	val input = readInput("Day06")

	check(part1(testInput).first == 41)
	part1(input).first.println()

	check(part2(testInput) == 6)
	part2(input).println()
}