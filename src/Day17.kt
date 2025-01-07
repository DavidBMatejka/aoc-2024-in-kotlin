import kotlin.math.pow

fun main() {

	data class State(
		var a: Long,
		var b: Long,
		var c: Long,
		var pointer: Int,
		val output: MutableList<Int> = mutableListOf<Int>(),
		val program: List<Int>,
	) {
		fun execute(instr: Int, operand: Int) {
			val comboOp = mapOf<Int, Long>(
				0 to 0,
				1 to 1,
				2 to 2,
				3 to 3,
				4 to a,
				5 to b,
				6 to c,
				7 to -1
			)
//			"a=$a, b=$b, c=$c, instr: $instr,$operand".println()
			when (instr) {
				0 -> {
					var numerator = a
					val exp = comboOp[operand]!!.toDouble()
					val denominator = 2.0.pow(exp)
					val erg = numerator / denominator
					a = erg.toLong()
					pointer += 2
				}

				1 -> {
					b = b.xor(operand.toLong())
					pointer += 2
				}

				2 -> {
					b = comboOp[operand]!!.mod(8).toLong()
					pointer += 2
				}

				3 -> {
					if (a != 0L) {
						pointer = operand
					} else {
						pointer += 2
					}
				}

				4 -> {
					b = b.xor(c)
					pointer += 2
				}

				5 -> {
					output.add(comboOp[operand]!!.mod(8))
					pointer += 2
				}

				6 -> {
					var numerator = a
					val exp = comboOp[operand]!!.toDouble()
					val denominator = 2.0.pow(exp)
					val erg = numerator / denominator
					b = erg.toLong()
					pointer += 2
				}

				7 -> {
					var numerator = a
					val exp = comboOp[operand]!!.toDouble()
					val denominator = 2.0.pow(exp)
					val erg = numerator / denominator
					c = erg.toLong()
					pointer += 2
				}
			}
		}

		fun run() {
			while (pointer + 1 in program.indices) {
				val instr = program[pointer]
				val operand = program[pointer + 1]
				this.execute(instr, operand)
			}
		}
	}

	fun part1(input: List<String>): String {
		val a = input[0].substringAfter(": ").toLong()
		val b = input[1].substringAfter(": ").toLong()
		val c = input[2].substringAfter(": ").toLong()
		val program = input.last().substringAfter(": ").split(",").map { it.toInt() }

		val state = State(a, b, c, 0, program = program)
		state.run()

		return state.output.joinToString(",")
	}

	fun part1AsTest(a: Long): List<Int> {
		val program = listOf(2,4,1,2,7,5,0,3,1,7,4,1,5,5,3,0)
		val state = State(a, 0, 0, 0, program = program)
		state.run()
		return state.output
	}



	fun part2(input: List<String>): Long {
		val program = input.last().substringAfter(": ").split(",").map { it.toInt() }

		fun getWays(start: Long, goalList: List<Int>): Set<Long> {
			return buildSet {
				for (i in 0..7L) {
					val a = (8 * start) + i
					var tested = part1AsTest(a)
					if (tested == goalList) {
						add(a)
					}
				}
			}
		}

		fun getWayRec(
			start: Long = 0L,
			currentList: List<Int> = listOf(0),
			index: Int = program.size - 2,
		): List<Long> {
			val solutions = mutableListOf<Long>()
			getWays(start, currentList).forEach {
				if (currentList.size < program.size) {
					val nextList = currentList.toMutableList().apply { addFirst(program[index]) }
					solutions.addAll(getWayRec(it, nextList, index = index - 1))
				}
				if (currentList == program) {
					solutions.add(it)
				}
			}
			return solutions
		}

		return getWayRec().min()
	}

	val testInput = readInput("Day17_test")
	val input = readInput("Day17")

	check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
	part1(input).println()
	part2(input).println()
}