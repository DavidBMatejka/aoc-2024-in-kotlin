fun main() {
	val dayNr = 24

	fun parseInput(input: List<String>): Map<String, String> {
		val gates = mutableMapOf<String, String>()
		input.takeWhile { it.isNotEmpty() }.forEach {
			val (dst, value) = it.split(": ")
			gates[dst] = value
		}

		input.dropWhile { it.isNotEmpty() }.drop(1).forEach {
			val (left, dst) = it.split(" -> ")
			gates[dst] = left
		}

		return gates
	}

	fun calcOutputs(originalInputGates: MutableMap<String, String>): String {
		fun MutableMap<String, String>.getOutputOf(cur: String): String {
			val prev = this[cur]!!
			if (prev.length == 1) {
				return prev
			}

			val (a, op, b) = prev.split(" ")
			val left = this.getOutputOf(a)
			val right = this.getOutputOf(b)
			return when (op) {
				"XOR" -> left.toInt().xor(right.toInt()).toString()
				"AND" -> left.toInt().and(right.toInt()).toString()
				"OR" -> left.toInt().or(right.toInt()).toString()
				else -> {
					throw (IllegalStateException("$prev doesn't contain a valid operation"))
				}
			}
		}

		return buildString {
			originalInputGates.filter { it.key.startsWith("z") }
				.toSortedMap()
				.forEach {
					val output = originalInputGates.toMutableMap().getOutputOf(it.key)
					append(output)
				}
		}.toString().reversed()
	}

	fun part1(input: List<String>): Long {
		val gates = parseInput(input).toMutableMap()
		return calcOutputs(gates).toLong(2)
	}

	fun pprint(originalInputGates: MutableMap<String, String>, gate: String, depth: Int = 1) {
		val (a, op, b) = originalInputGates[gate]!!.split(" ")
		println("$gate: $op ($a $b)")
		if (depth == 5) {
			return
		}
		if (!b.startsWith("x") && !b.startsWith("y")) {
			print("    ".repeat(depth))
			pprint(originalInputGates, b, depth + 1)
		}
		if (!a.startsWith("x") && !a.startsWith("y")) {
			print("    ".repeat(depth))
			pprint(originalInputGates, a, depth + 1)
		}
	}

	fun testCircuit(gates: Map<String, String>): String {
		return buildString {
			append("found wrong gate at: ")
			val mutable = gates.toMutableMap()
			mutable.filter { it.key.startsWith("x") }.map { mutable[it.key] = "0" }
			mutable.filter { it.key.startsWith("y") }.keys.forEachIndexed { i, key ->
				mutable.filter { it.key.startsWith("y") }.map { mutable[it.key] = "0" }
				mutable[key] = "1"
				val output = calcOutputs(mutable)
//				output.println()
				if (output.reversed().indexOf("1") != i) append("$i, ")
			}
		}
	}

	fun MutableMap<String, String>.swap(s1: String, s2: String) {
		val tmp = this[s1]!!
		this[s1] = this[s2]!!
		this[s2] = tmp
	}

	fun part2(input: List<String>) {
		val gates = parseInput(input)
		val mutableGates = gates.toMutableMap()

// 	use this functions to find the gates with wrong cables
//		testCircuit(mutableGates).println()

//		use prettyprint to observe the error gates and compare with gates that are correct
//		to see how it should be wired
// 		pprint(mutableGates, "z05")
//		pprint(mutableGates, "z06")
//		pprint(mutableGates, "z07")

//		repeat for all error gates

//		test again after each swap to see if it worked
//		testCircuit(mutableGates).println()
		mutableGates.swap("dbp", "fdv")
		mutableGates.swap("z15", "ckj")
		mutableGates.swap("kdf", "z23")
		mutableGates.swap("rpp", "z39")

		listOf(
			"dbp", "fdv",
			"z15", "ckj",
			"kdf", "z23",
			"rpp", "z39",
		).sorted().joinToString(",").println()
	}

	val testInput = readInput("Day${dayNr}_test")
	val input = readInput("Day${dayNr}")

	check(part1(testInput) == 2024L)
	part1(input).println()
	part2(input)
}