import kotlin.math.pow

fun main() {

	data class State(
		var a: Int,
		var b: Int,
		var c: Int,
		var pointer: Int,
		val output: MutableList<Int> = mutableListOf<Int>(),
		val program: List<Int>,
	) {
		fun execute(instr: Int, operand: Int) {
			val comboOp = mapOf<Int, Int>(
				0 to 0,
				1 to 1,
				2 to 2,
				3 to 3,
				4 to a,
				5 to b,
				6 to c,
				7 to -1
			)
			when (instr) {
				0 -> {
					var numerator = a
					val exp = comboOp[operand]!!.toDouble()
					val denominator = 2.0.pow(exp)
					val erg = numerator / denominator
					a = erg.toInt()
					pointer += 2
				}

				1 -> {
					b = b.xor(operand)
					pointer += 2
				}

				2 -> {
					b = comboOp[operand]!!.mod(8)
					pointer += 2
				}

				3 -> {
					if (a != 0) {
						pointer = operand
					} else {
						pointer += 1
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
					b = erg.toInt()
					pointer += 2
				}

				7 -> {
					var numerator = a
					val exp = comboOp[operand]!!.toDouble()
					val denominator = 2.0.pow(exp)
					val erg = numerator / denominator
					c = erg.toInt()
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
		val a = input[0].substringAfter(": ").toInt()
		val b = input[1].substringAfter(": ").toInt()
		val c = input[2].substringAfter(": ").toInt()
		val program = input.last().substringAfter(": ").split(",").map { it.toInt() }

		val state = State(a, b, c, 0, program = program)
		state.run()

		return state.output.joinToString(",")
	}

	fun part2(input: List<String>): Int {

		return -1
	}

	val testInput = readInput("Day17_test")
	val input = readInput("Day17")

	check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
	part1(input).println()

	check(part2(testInput) == -1)
	part2(input).println()
}
