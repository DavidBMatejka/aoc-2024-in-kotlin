fun main() {

	data class FileBlock(var id: Int, var length: Int) {
		fun shrinkBy(length: Int) {
			this.length -= length
		}
		override fun toString(): String {
			if (id == -1) return ".".repeat(length)
			return "$id".repeat(length)
		}
		fun split(): List<FileBlock> {
			val splitted = mutableListOf<FileBlock>()
			(0..<length).forEach { i ->
				splitted.add(FileBlock(id, 1))
			}
			return splitted
		}
	}

	fun part1(input: List<String>): Long {
		val diskMap = input[0]

		var id = 0
		val freespaceRemoved = mutableListOf<Int>()
		val unpacked = buildList {
			diskMap.forEachIndexed { i, c ->
				if (i % 2 == 0) {
					(1..c.digitToInt()).forEach {
						add(id)
						freespaceRemoved.add(id)
					}
					id++
				} else {
					(1..c.digitToInt()).forEach { add(-1) }
				}
			}
		}

		val compact = buildList<Int> {
			unpacked.forEach { id ->
				if (freespaceRemoved.isNotEmpty()) {
					if (id != -1) {
						add(freespaceRemoved.removeFirst())
					} else {
						add(freespaceRemoved.removeLast())
					}
				}
			}
		}

		var sum = 0L
		compact.forEachIndexed { i, id ->
			sum += i * id
		}

		return sum
	}

	fun tryToFit(list: MutableList<FileBlock>, fileBlock: FileBlock): MutableList<FileBlock> {
		val index = list.indexOf(fileBlock)
		for (i in list.indices) {
			if (index <= i) return list

			val item = list[i]
			if (item.id == -1 && item.length == fileBlock.length) {
				item.id = fileBlock.id
				list[index].id = -1
				return list
			}
			if (item.id == -1 && item.length > fileBlock.length) {
				list.add(i, fileBlock)
				item.shrinkBy(fileBlock.length)
				val ind = list.indexOfLast { it -> it.id == fileBlock.id }
				list[ind].id = -1
				return list
			}
		}

		return list
	}

	fun part2(input: List<String>): Long {
		val diskMap = input[0]

		var id = 0
		val freespaceRemoved = mutableListOf<FileBlock>()
		var unpacked = buildList {
			diskMap.forEachIndexed { i, c ->
				if (i % 2 == 0) {
					add(FileBlock(id, c.digitToInt()))
					freespaceRemoved.add(FileBlock(id, c.digitToInt()))
					id++
				} else if (c.digitToInt() != 0){
					add(FileBlock(-1, c.digitToInt()))
				}
			}
		}.toMutableList()

		for (fileBlock in freespaceRemoved.reversed()) {
			unpacked = tryToFit(unpacked, fileBlock)
		}

		var sum = 0L
		val splitted = mutableListOf<FileBlock>()
		unpacked.forEach {
				splitted.addAll(it.split())
		}
		splitted.forEachIndexed { index, item ->
			if (item.id != -1) sum += item.id * index
		}

		return sum
	}

	val testInput = readInput("Day09_test")
	val input = readInput("Day09")

	check(part1(testInput) == 1928L)
	part1(input).println()

	check(part2(testInput) == 2858L)
	part2(input).println()
}