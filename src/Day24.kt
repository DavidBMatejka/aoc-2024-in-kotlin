fun main() {
    val dayNr = 24

    fun parseInput(input: List<String>): MutableMap<String, String> {
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

    fun getOutput(gates: MutableMap<String, String>, cur: String): String {
        val prev = gates[cur]!!
        if (prev.length == 1) { return prev }

        val (a, op, b) = prev.split(" ")
        val left = getOutput(gates, a)
        val right = getOutput(gates, b)
        gates[a] = left
        gates[b] = right
        when(op) {
            "XOR" -> gates[cur] = left.toInt().xor(right.toInt()).toString()
            "AND" -> gates[cur] = left.toInt().and(right.toInt()).toString()
            "OR" -> gates[cur] = left.toInt().or(right.toInt()).toString()
        }

        return gates[cur]!!
    }

    fun part1(input: List<String>): Long {
        val gates = parseInput(input)
        val tmp =  buildString {
            gates.filter { it.key.startsWith("z") }
                .toSortedMap()
                .forEach {
                append(getOutput(gates, it.key))
            }
        }.toString().reversed().toLong(2)
        return tmp
    }

    fun part2(input: List<String>): Int {

        return -1
    }

    val testInput = readInput("Day${dayNr}_test")
    val input = readInput("Day${dayNr}")

    check(part1(testInput) == 2024L)
    part1(input).println()

	check(part2(testInput) == -1)
    part2(input).println()
}
