fun main() {
	val dayNr = 23

	fun parseInput(input: List<String>): Map<String, MutableSet<String>> {
		val graph = mutableMapOf<String, MutableSet<String>>()
		input.forEach { line ->
			val (src, dst) = line.split("-")
			graph[src]?.add(dst) ?: mutableSetOf(dst).also { graph[src] = it }
			graph[dst]?.add(src) ?: mutableSetOf(src).also { graph[dst] = it }
		}

		return graph.toSortedMap()
	}

	fun getInterconnected(graph: Map<String, Set<String>>): MutableSet<String> {
		val interconnected = mutableSetOf<String>()
		graph.forEach {
			it.value.forEach { n1 ->
				it.value.forEach { n2 ->
					if (n1 != n2) {
						if (n1 in graph[n2]!! && n2 in graph[n1]!!) {
							interconnected.add(listOf(it.key, n1, n2).sorted().joinToString(","))
						}
					}
				}
			}
		}
		return interconnected
	}

	fun part1(input: List<String>): Int {
		val graph = parseInput(input)
		val interConnected = getInterconnected(graph)

		return interConnected.filter {
			it.split(",").any {
				it[0] == 't'
			}
		}.size
	}

	fun part2(input: List<String>): String {
		val graph = parseInput(input)
		val interConnected = getInterconnected(graph)

		val occurences = mutableMapOf<String, Int>()
		interConnected.forEach {
			val node = it.split(",").first()
			occurences[node] = (occurences[node]?: 0) + 1
		}

		val mostFrequent = occurences.toList().maxByOrNull { it.second }!!.first
		val erg = mutableSetOf<String>()
		interConnected.filter {
			it.split(",").first() == mostFrequent
		}.forEach {
			erg.addAll(it.split(","))
		}

		return erg.sorted().joinToString(",")
	}

	val testInput = readInput("Day${dayNr}_test")
	val input = readInput("Day${dayNr}")

	check(part1(testInput) == 7)
	part1(input).println()

	check(part2(testInput) == "co,de,ka,ta")
	part2(input).println()
}
