fun main() {
	data class Point(val x: Int, val y: Int) {
		fun getMirroredTo(center: Point): Point {
			val dx = center.x - x
			val dy = center.y - y

			return Point(x + 2 * dx, y + 2 * dy)
		}

		fun getPointInLine(center: Point, width: IntRange, height: IntRange): MutableSet<Point> {
			val dx = center.x - x
			val dy = center.y - y

			val points = mutableSetOf<Point>()

			var next = Point(this.x, this.y)
			while (next.x in width && next.y in height) {
				points.add(next)
				next = Point(next.x + dx, next.y + dy)
			}

			next = Point(this.x, this.y)
			while (next.x in width && next.y in height) {
				points.add(next)
				next = Point(next.x + dx, next.y + dy)
			}

			return points
		}

	}

	fun getAntennasPositions(input: List<String>): MutableMap<Char, MutableList<Point>> {
		val antennas = mutableMapOf<Char, MutableList<Point>>()
		input.forEachIndexed { i, line ->
			line.forEachIndexed { j, c ->
				if (c != '.') {
					antennas[c]?.add(Point(j, i)) ?: mutableListOf(
						Point(
							j,
							i
						)
					).also { antennas[c] = it }
				}
			}
		}
		return antennas
	}

	fun part1(input: List<String>): Int {
		val antennas = getAntennasPositions(input)

		val antinodes = mutableSetOf<Point>()
		for (antennaList in antennas.values) {
			for (a in antennaList) {
				for (b in antennaList) {
					if (a != b) {
						val antinode = a.getMirroredTo(b)
						if (antinode.x in input[0].indices && antinode.y in input.indices) {
							antinodes.add(a.getMirroredTo(b))
						}
					}
				}
			}
		}

		return antinodes.size
	}

	fun part2(input: List<String>): Int {
		val antennas = getAntennasPositions(input)

		val antinodes = mutableSetOf<Point>()
		for (antennaList in antennas.values) {
			for (a in antennaList) {
				for (b in antennaList) {
					if (a != b) {
						antinodes.addAll(a.getPointInLine(b, input[0].indices, input.indices))
					}
				}
			}
		}

		return antinodes.size
	}

	val testInput = readInput("Day08_test")
	val input = readInput("Day08")

	check(part1(testInput) == 14)
	part1(input).println()

	check(part2(testInput) == 34)
	part2(input).println()
}
