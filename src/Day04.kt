fun main() {

	fun searchHorizontalAt(grid: MutableList<String>, i: Int, j: Int): Int {
		if (grid[i][j] != 'X') {
			return 0
		}

		var sum = 0
		// R
		if ((j + 1) < grid[0].length && grid[i][j + 1] == 'M' &&
			(j + 2) < grid[0].length && grid[i][j + 2] == 'A' &&
			(j + 3) < grid[0].length && grid[i][j + 3] == 'S'
		) {
			sum++
		}
		// L
		if ((j - 1) >= 0 && grid[i][j - 1] == 'M' &&
			(j - 2) >= 0 && grid[i][j - 2] == 'A' &&
			(j - 3) >= 0 && grid[i][j - 3] == 'S'
		) {
			sum++
		}
		// U
		if ((i - 1) >= 0 && grid[i - 1][j] == 'M' &&
			(i - 2) >= 0 && grid[i - 2][j] == 'A' &&
			(i - 3) >= 0 && grid[i - 3][j] == 'S'
		) {
			sum++
		}
		// D
		if ((i + 1) < grid.size && grid[i + 1][j] == 'M' &&
			(i + 2) < grid.size && grid[i + 2][j] == 'A' &&
			(i + 3) < grid.size && grid[i + 3][j] == 'S'
		) {
			sum++
		}

		return sum
	}

	fun searchDiagonalAt(grid: MutableList<String>, i: Int, j: Int): Int {
		if (grid[i][j] != 'X') {
			return 0
		}

		var sum = 0

		// DR
		if ((i + 1) < grid.size && (j + 1) < grid[0].length && grid[i + 1][j + 1] == 'M' &&
			(i + 2) < grid.size && (j + 2) < grid[0].length && grid[i + 2][j + 2] == 'A' &&
			(i + 3) < grid.size && (j + 3) < grid[0].length && grid[i + 3][j + 3] == 'S'
		) {
			sum++
		}

		// DL
		if ((i + 1) < grid.size && (j - 1) >= 0 && grid[i + 1][j - 1] == 'M' &&
			(i + 2) < grid.size && (j - 2) >= 0 && grid[i + 2][j - 2] == 'A' &&
			(i + 3) < grid.size && (j - 3) >= 0 && grid[i + 3][j - 3] == 'S'
		) {
			sum++
		}

		// UR
		if ((i - 1) >= 0 && (j + 1) < grid[0].length && grid[i - 1][j + 1] == 'M' &&
			(i - 2) >= 0 && (j + 2) < grid[0].length && grid[i - 2][j + 2] == 'A' &&
			(i - 3) >= 0 && (j + 3) < grid[0].length && grid[i - 3][j + 3] == 'S'
		) {
			sum++
		}

		// UL
		if ((i - 1) >= 0 && (j - 1) >= 0 && grid[i - 1][j - 1] == 'M' &&
			(i - 2) >= 0 && (j - 2) >= 0 && grid[i - 2][j - 2] == 'A' &&
			(i - 3) >= 0 && (j - 3) >= 0 && grid[i - 3][j - 3] == 'S'
		) {
			sum++
		}

		return sum
	}

	fun part1(input: List<String>): Int {
		val grid = MutableList(input.size) { "" }

		var sum = 0
		input.forEachIndexed { i, it ->
			grid[i] = it
		}

		for (i in 0..grid.size - 1) {
			for (j in 0..grid[0].length - 1) {
				sum += searchHorizontalAt(grid, i, j)
				sum += searchDiagonalAt(grid, i, j)
			}
		}

		return sum
	}

	fun searchMAS(grid: MutableList<String>, i: Int, j: Int): Int {
		if (grid[i][j] != 'A') {
			return 0
		}

		var sum = 0
		if (i + 1 >= grid.size) return 0
		if (j + 1 >= grid.size) return 0
		if (i - 1 < 0) return 0
		if (j - 1 < 0) return 0

		if ( grid[i + 1][j + 1] == 'M' && grid[i - 1][j - 1] == 'S'
			|| grid[i + 1][j + 1] == 'S' && grid[i - 1][j - 1] == 'M'
		) {
			if ( grid[i + 1][j - 1] == 'M' && grid[i - 1][j + 1] == 'S'
				|| grid[i + 1][j - 1] == 'S' && grid[i - 1][j + 1] == 'M'
			) {
				sum++
			}
		}

		return sum
	}

	fun part2(input: List<String>): Int {
		val grid = MutableList(input.size) { "" }

		input.forEachIndexed { i, it ->
			grid[i] = it
		}

		var sum = 0
		for (i in 0..grid.size - 1) {
			for (j in 0..grid[0].length - 1) {
				sum += searchMAS(grid, i, j)
			}
		}

		return sum
	}

	val testInput1= readInput("Day04_test1")
	check(part1(testInput1) == 18)
	val testInput2 = readInput("Day04_test2")
	check(part2(testInput2) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}