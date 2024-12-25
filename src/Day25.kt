fun main() {
    val dayNr = 25

    fun parseInput(input: List<String>): Pair<List<MutableList<String>>, List<MutableList<String>>> {
        val locksAndKeys =  input.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isEmpty()) acc.add(mutableListOf())
            else {
               acc.last().add(line)
            }
            acc
        }

        val keys = locksAndKeys.filter { it[0].contains(".") }
        val locks = locksAndKeys.filter {it.last().contains(".")}
        return Pair(locks, keys)
    }

    fun List<List<String>>.toHeights(): List<List<Int>> {
        val heights = mutableListOf<MutableList<Int>>()
        this.forEach { lock ->
            val tmpHeights = mutableListOf<Int>()
            for (x in lock[0].indices) {
                var sum = 0
                for (y in lock.indices) {
                    if(lock[y][x] == '#') sum++
                }
                tmpHeights.add(sum - 1)
            }
            heights.add(tmpHeights)
        }

        return heights
    }

    fun List<Int>.notOverlaps(other: List<Int>): Boolean {
        for (i in this.indices) {
            if (this[i] + other[i] > this.size) return false
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val (locks, keys) = parseInput(input)
        val lockHeights = locks.toHeights()
        val keyHeights = keys.toHeights()

        var sum = 0
        lockHeights.forEach { lockHeight ->
            keyHeights.forEach { keyHeight ->
                if (lockHeight.notOverlaps(keyHeight)) {
                    sum++
                }
            }
        }

        return sum
    }

    val testInput = readInput("Day${dayNr}_test")
    val input = readInput("Day${dayNr}")

    check(part1(testInput) == 3)
    part1(input).println()
}