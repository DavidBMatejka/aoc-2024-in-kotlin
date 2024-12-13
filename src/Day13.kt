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

	fun bfs(machine: Machine): Int {
		val costSoFar = mutableMapOf<Pos, Long>().withDefault { Int.MAX_VALUE.toLong() }
		val q = PriorityQueue<Pos>(compareBy { costSoFar[it] })
		val start = Pos(0, 0)
		q.add(start)
		costSoFar[start] = 0
		val neighbours = listOf(machine.A, machine.B)

		while (q.isNotEmpty()) {
			val current = q.poll()

			if (current.x > machine.prize.x || current.y > machine.prize.y) {
				continue
			}

			if (current == machine.prize) {
				return costSoFar[current]!!.toInt()
			}

			for (shift in neighbours) {
				val next = Pos(current.x + shift.dx, current.y + shift.dy)
				val newCost = costSoFar.getValue(current) + shift.cost
				if (newCost < costSoFar.getValue(next)) {
					costSoFar[next] = newCost
					q.add(next)
				}
			}

		}

		return 0
	}

	fun part1(input: List<String>): Int {
		val machines = parseInput(input)

		var sum = 0
		machines.forEach { machine ->
			sum += bfs(machine)
		}

		return sum
	}

	fun part2(input: List<String>): Int {

		return -1
	}

	val testInput = readInput("Day13_test")
	val input = readInput("Day13")

	check(part1(testInput) == 480)
	part1(input).println()
//
//	check(part2(readInput("Day12_test")) == -1)
//	part2(input).println()
}
