import java.util.*

fun main() {
	val dayNr = 21

	fun <V> List<V>.permutations(): List<List<V>> {
		val retVal: MutableList<List<V>> = mutableListOf()

		fun generate(k: Int, list: List<V>) {
			// If only 1 element, just output the array
			if (k == 1) {
				retVal.add(list.toList())
			} else {
				for (i in 0 until k) {
					generate(k - 1, list)
					if (k % 2 == 0) {
						Collections.swap(list, i, k - 1)
					} else {
						Collections.swap(list, 0, k - 1)
					}
				}
			}
		}

		generate(this.count(), this.toList())
		return retVal
	}

	val dirs = mapOf(
		'>' to Pair(1, 0),
		'^' to Pair(0, -1),
		'v' to Pair(0, 1),
		'<' to Pair(-1, 0),
	)

	val numpad = listOf(
		"789",
		"456",
		"123",
		"X0A"
	)

	val dirpad = listOf(
		"X^A",
		"<v>"
	)

	data class Pos(val x: Int, val y: Int) {
		var char: Char = 'z'
		var path: String = ""

		fun getNeighbours(pad: List<String>, perm: Map<Char, Pair<Int, Int>>): List<Pos> {
			val neighbours = mutableListOf<Pos>()

			perm.forEach {
				val (dx, dy) = it.value
				val next = Pos(x + dx, y + dy)
				if (next.x in pad[0].indices && next.y in pad.indices) {
					val nextChar = pad[next.y][next.x]
					if (nextChar != 'X') {
						next.char = nextChar
						next.path = "${this.path}${it.key}"
						neighbours.add(next)
					}
				}
			}

			return neighbours
		}
	}


	fun bfs(pad: List<String>, start: Pos, end: Char, perm: Map<Char, Pair<Int, Int>>): Pos {
		val q = mutableListOf<Pos>()
		q.add(start)
		val visited = mutableSetOf<Pos>()

		while (q.isNotEmpty()) {
			val curr = q.removeFirst()

			if (curr.char == end) {
				return curr
			}

			visited.add(curr)
			for (neighbour in curr.getNeighbours(pad, perm)) {
				if (neighbour !in visited) {
					visited.add(neighbour)
					q.add(neighbour)
				}
			}

		}

		return Pos(-1, -1)
	}

	fun getSequence(pad: List<String>, code: String): List<String> {
		val start = if (pad == numpad) {
			Pos(2, 3)
		} else {
			Pos(2, 0)
		}

		val perms = dirs.toList().permutations().map { it.toMap() }

		val sequences = mutableListOf<String>()
		perms.forEach { perm ->
			var pos = bfs(pad, start, code[0], perm)
			var sequence = pos.path + "A"
			pos.path = ""
			for (i in code.indices) {
				if (i == 0) continue
				pos = bfs(pad, pos, code[i], perm)
				sequence += pos.path + "A"
				pos.path = ""
			}
			sequences.add(sequence)
		}
		return sequences
	}

	fun part1(input: List<String>): Int {
		var sum = 0
		for (line in input) {
			val length = buildList {
				getSequence(numpad, line).forEach {
					for (s in (getSequence(dirpad, it))) {
						add(getSequence(dirpad, s))
					}
				}
			}.flatten().minBy { it.length }.length

			val num = line.dropLast(1).toInt()
			sum += num * length
		}

		return sum
	}

	fun part2(input: List<String>): Int {

		return -1
	}

	val testInput = readInput("Day${dayNr}_test")
	val input = readInput("Day${dayNr}")

	check(part1(testInput) == 126384)
	part1(input).println()
//
//	check(part2(testInput) == -1)
//	part2(input).println()
}
