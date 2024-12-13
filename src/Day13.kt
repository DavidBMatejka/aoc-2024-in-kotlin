fun main() {

	data class Shift(val dx: Long, val dy: Long, val cost: Long)
	data class Pos(val x: Long, val y: Long)
	data class Machine(val A: Shift, val B: Shift, val prize: Pos)

	fun parseInput(input: List<String>): List<Machine> {
		val blocks =
			input.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
				if (line.isBlank()) {
					acc.add(mutableListOf())
				} else {
					acc.last().add(line)
				}
				acc
			}

		val machines = buildList {
			blocks.forEach { block ->
				val (dxA, dyA) = block[0].substringAfter(": ").split(", ").map {
					it.substringAfter("+").toLong()
				}
				val (dxB, dyB) = block[1].substringAfter(": ").split(", ").map {
					it.substringAfter("+").toLong()
				}
				val (x, y) = block[2].substringAfter(": ").split(", ").map {
					it.substringAfter("=").toLong()
				}
				val shiftA = Shift(dxA, dyA, 3)
				val shiftB = Shift(dxB, dyB, 1)
				val prize = Pos(x, y)
				add(Machine(shiftA, shiftB, prize))
			}
		}
		return machines
	}

	fun calcFactors(machine: Machine, extra: Long): Pair<Long, Long> {
		/*
		I	94 * a + 22 * b = 8400
		II	34 * a + 67 * b = 5400

		I 	94 * 34 * a + 22 * 34 * b = 8400 * 34
		II	34 * 94 * a + 67 * 94 * b = 5400 * 94


	II - I b = (5400 * 94 - 8400 * 34) / (67 * 94 - 22 * 34)
					-> b = 40

		generally:
		i ax * a + bx * b = prize.x
		i ay * a + by * b = prize.y
		ii - i b = (prize.y * ax - prize.x * ay) / (by * ax - bx * ay)
		*/

		val px = machine.prize.x + extra
		val py = machine.prize.y + extra
		val ax = machine.A.dx
		val ay = machine.A.dy
		val bx = machine.B.dx
		val by = machine.B.dy

		val b = (py * ax - px * ay) / (by * ax - bx * ay)
		val a = (py - by * b) / ay

		val first = ax * a + bx * b
		val second = ay * a + by * b
		if (first != px || second != py) {
			return Pair(0, 0)
		}

		return Pair(a,b)
	}


	fun part1(input: List<String>): Long {
		val machines = parseInput(input)

		var sum = 0L
		machines.forEach { machine ->
			val (a, b) = calcFactors(machine, 0)
			sum += 3 * a + b
		}

		return sum
	}


	fun part2(input: List<String>): Long {
		val machines = parseInput(input)
		var sum = 0L

		machines.forEach { machine ->
			machine.println()
			val (a, b) = calcFactors(machine, 10000000000000L)
			sum += 3 * a + b
		}

		return sum
	}

	val testInput = readInput("Day13_test")
	val input = readInput("Day13")


	check(part1(testInput) == 480L)
	part1(input).println()

	"part2:".println()
	part2(testInput).println()
	part2(input).println()
}