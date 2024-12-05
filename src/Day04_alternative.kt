fun main() {
	fun transpose(grid: MutableList<String>): MutableList<String> {
		val transposed = MutableList(grid.size) { "" }
		grid.forEach {
			for (j in 0..<grid.size) {
				transposed[j] = transposed[j] + it[j]
			}
		}
		return transposed
	}


	/* thoughts for 45° rotation to the left

	rotated grid should output the abc example in the correct order
	val example = mutableListOf(
		"dba",
		"gec",
		"ihf")

	so grid should look something like this:

	.a.
	.bc
	def
	ge.
	i..

	representing this as with the need positions and positioning it so that
	sensible looping is possible to spot:

		.			.		grid[0][2]
		.	    grid[0][1]	grid[1][2]
	grid[0][0] 	grid[1][1]	grid[2][2]
	grid[1][0]  grid[2][1]		.
	grid[2][0]		.			.


	filling up the corners:

	grid[-2][0]		grid[-1][1]	grid[0][2]
	grid[-1][0]		grid[0][1]	grid[1][2]
	grid[0][0]		grid[1][1]	grid[2][2]
	grid[1][0] 		grid[2][1]	grid[3][2]
	grid[2][0]		grid[3][1]	grid[4][2]
	*/
	fun rotate45(grid: MutableList<String>): List<String> {
		val width = grid[0].length
		//todo height can be optimzed; for 3x3 there's an extra row
		val height = grid.size * 2
		val rotated = MutableList(height) { MutableList(width) { '.' } }
		for (i in 0..<height) {
			for (j in 0..<width) {
				val l = i - (width - 1) + j
				if (l < 0 || l > grid.size - 1) {
					continue
				}
				rotated[i][j] = grid[l][j]
			}
		}
		return rotated.map { it.joinToString("") }
	}

	fun part1(input: List<String>): Int {

		val grid = MutableList(input.size) { "" }
		input.forEachIndexed { i, it ->
			grid[i] = it
		}

		var sum = 0
		grid.forEach { line ->
			line.windowed(4) {
				if (it.toString() == "XMAS" || it.toString() == "SAMX") {
					sum++
				}
			}
		}
		rotate45(grid).forEach { line ->
			line.windowed(4) {
				if (it.toString() == "XMAS" || it.toString() == "SAMX") {
					sum++
				}
			}
		}
		val transposed = transpose(grid)
		transposed.forEach { line ->
			line.windowed(4) {
				if (it.toString() == "XMAS" || it.toString() == "SAMX") {
					sum++
				}
			}
		}
		rotate45(transposed).forEach { line ->
			line.windowed(4) {
				if (it.toString() == "XMAS" || it.toString() == "SAMX") {
					sum++
				}
			}
		}
		return sum
	}

//	fun part2(input: List<String>): Int {
//		val grid = MutableList(input.size) { "" }
//		input.forEachIndexed { i, it ->
//			grid[i] = it
//		}
//		rotate45(grid).forEach(::println)
//		return -1
//	}

	val testInput1 = readInput("Day04_test1")
	check(part1(testInput1) == 18)
//	val testInput2 = readInput("Day04_test2")
//	part2(testInput2)
//	check(part2(testInput2) == 9)

	val input = readInput("Day04")
	part1(input).println()
//	part2(input).println()


}