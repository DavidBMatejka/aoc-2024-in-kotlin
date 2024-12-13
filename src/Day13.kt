import java.util.PriorityQueue

fun main() {

	data class Shift(val dx: Int, val dy: Int, val cost: Int)
	data class Pos(val x: Int, val y: Int)
	data class Machine(val A: Shift, val B: Shift, val prize: Pos)

	fun parseInput(input: List<String>, ): List<Machine> {
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
			var tmp = bfs(machine)
			if (tmp == Int.MAX_VALUE) tmp = 0
//			tmp.println()
			sum += tmp
		}

		return sum
	}

	fun part2(input: List<String>): Int {
		val machines = parseInput(input,10000000000000)
		var sum = 0
		machines.forEachIndexed { nr, machine ->
			if (nr == 250) {
				println(machine)
			}
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
					if ((i * machine.A.dx + b * machine.B.dx) == machine.prize.x ) {
						val tmp = 3 * i + b
						if (tmp < min) min = tmp.toInt()
						foundSolution = true
					}
				}
				i++
			}
//			"found for $machine".println()
			if (min == Int.MAX_VALUE) {
				min = 0
			}
//			min.println()
			sum += min
		}

		return sum
	}

	val testInput = readInput("Day13_test2")
	val input = readInput("Day13")


	"part1:".println()
//	part1(testInput).println()
//	part1(input).println()
//	println()

	"part2:".println()
//	part2(testInput).println()
	part2(input).println()
}
