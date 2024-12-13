import java.util.PriorityQueue

fun main() {

	data class Shift(val dx: Int, val dy: Int, val cost: Int)
	data class Pos(val x: Int, val y: Int)
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
					it.substringAfter("+").toInt()
				}
				val (dxB, dyB) = block[1].substringAfter(": ").split(", ").map {
					it.substringAfter("+").toInt()
				}
				val (x, y) = block[2].substringAfter(": ").split(", ").map {
					it.substringAfter("=").toInt()
				}
				val shiftA = Shift(dxA, dyA, 3)
				val shiftB = Shift(dxB, dyB, 1)
				val prize = Pos(x, y)
				add(Machine(shiftA, shiftB, prize))
			}
		}
		return machines
	}


	fun calcSolVec(machine: Machine): Shift {
		var solVec = Shift(0, 0, 0)
		var i = 0
		var foundSolution = false
		var k = 0
		var min = Int.MAX_VALUE
		while (true) {
			val b = (machine.prize.x - i * machine.A.dx) / machine.B.dx
			if (i * machine.A.dx > machine.prize.x) {
				break
			}
			if (foundSolution) {
				k++
			}
			if (k == 1000000) {
				break
			}
			if ((i * machine.A.dy + b * machine.B.dy).toInt() == machine.prize.y) {
				if ((i * machine.A.dx + b * machine.B.dx) == machine.prize.x) {
					val tmp = 3 * i + b
					if (tmp < min) {
						min = tmp.toInt()
						solVec = Shift(
							i * machine.A.dx + b * machine.B.dx,
							i * machine.A.dy + b * machine.B.dy,
							tmp
						)
					}
					foundSolution = true
				}
			}
			i++
		}
		if (min == Int.MAX_VALUE) {
			min = 0
		}

		return Shift(machine.prize.x, machine.prize.y, min)
	}


	fun part1(input: List<String>): Int {
		val machines = parseInput(input)

		var sum = 0
		machines.forEach { machine ->
			sum += calcSolVec(machine).cost
		}

		return sum
	}



	fun part2(input: List<String>): Int {
		val machines = parseInput(input)
		var sum = 0
		machines.forEach { machine ->
			val solVec = calcSolVec(machine)
			solVec.println()

			val tmp = solVec.cost
			val modX =
				Math.floorMod(machine.prize.x + 10000000000000, machine.prize.x).toLong()
			val fittingX =
				(machine.prize.x + 10000000000000 - modX) / machine.prize.x

			val modY =
				Math.floorMod(machine.prize.y + 10000000000000, machine.prize.y).toLong()
			val fittingY =
				(machine.prize.y + 10000000000000 - modY) / machine.prize.y

			modX.println()
			modY.println()

			fittingX.println()
			fittingY.println()
			val ggt = gcd(fittingX, fittingY)
			"ggt: $ggt".println()

		}

		return sum
	}

	val testInput = readInput("Day13_test")
	val input = readInput("Day13")


	"part1:".println()
//	part1(testInput).println()
	check(part1(testInput) == 480)
	part1(input).println()

	"part2:".println()
	part2(testInput).println()
//	part2(input).println()
}
