import kotlin.math.abs

fun main() {
    val dayNr = 20

    fun transpose(grid: MutableList<String>): MutableList<String> {
        val transposed = MutableList(grid.size) { "" }
        grid.forEach {
            for (j in 0..<grid.size) {
                transposed[j] = transposed[j] + it[j]
            }
        }
        return transposed
    }

    fun parseInput(input: List<String>): MutableList<CharArray> {
        val map = mutableListOf<CharArray>()
        for (line in input) {
            map.add(line.toCharArray())
        }
        return map
    }

    data class Shift(val dx: Int, val dy: Int)
    val dirs = listOf(
        Shift(1, 0),
        Shift(-1, 0),
        Shift(0, 1),
        Shift(0, -1),
    )

    data class Position(val x: Int, val y: Int) {
        var steps = 0L
        fun getNeighbours(map: List<CharArray>): List<Position> {
            val neighbours = mutableListOf<Position>()
            for (dir in dirs) {
                val (dx, dy) = dir
                val next = Position(x + dx, y + dy)
                if (next.x in map[0].indices && next.y in map.indices
                    && map[next.y][next.x] != '#'
                    ) {
                    next.steps = steps + 1
                    neighbours.add(next)
                }
            }
            return neighbours
        }


    }

    fun bfs(map: MutableList<CharArray>): MutableMap<Position, Long> {
        var start = Position(0,0)
        var end = Position(0,0)
        map.forEachIndexed { y, line ->
            val xS = line.indexOf('S')
            val xE = line.indexOf('E')
            if (xS != -1) start = Position(xS, y)
            if (xE != -1) end = Position(xE, y)
        }

        val q = mutableListOf<Position>()
        q.add(start)
        val posToCost = mutableMapOf<Position, Long>()
        posToCost[start] = 0

        while (q.isNotEmpty()) {
            val current = q.removeFirst()

            if (current.x == end.x && current.y == end.y) {
                posToCost[current] = current.steps
                return posToCost
            }

            val neighbours = current.getNeighbours(map)
            neighbours.forEach { next ->
                if (next !in posToCost) {
                    posToCost[next] = next.steps
                    q.add(next)
                }
            }

        }

        return mutableMapOf()
    }

    fun part1(input: List<String>): Int {
        val map = parseInput(input)
        val posToCost = bfs(map)

//        posToCost.forEach { it.println() }

//        input.forEachIndexed { y, line ->
//            print("$y: ")
//            line.windowed(3).forEachIndexed { i, it ->
//                print("$it ")
////                if (it == ".#.")
//            }
//            println()
//        }

        val shortcuts = mutableListOf<Long>()
        input.forEachIndexed { y, line ->
            line.windowed(3).forEachIndexed { i, it ->
                if (it == ".#." || it == "S#." || it == ".#S" || it == "E#." || it == ".#E") {
                    val start = posToCost[Position(i, y)]!!
                    val end = posToCost[Position(i + 2, y)]!!
                    // always needs to take two steps for the shortcut so -2
                    val shortcutCost = abs(end - start) - 2
                    shortcuts.add(shortcutCost)
//                    println("shortcut at ($i,$y):costToStart=$start, costToEnd:$end -> ${abs(start - end) - 2} less steps")
                }
            }
        }

        val transposed = transpose(input.toMutableList())
        transposed.forEachIndexed { y, line ->
            line.windowed(3).forEachIndexed { i, it ->
                if (it == ".#." || it == "S#." || it == ".#S" || it == "E#." || it == ".#E") {
                    // neet to flip x and y since map is transposed
                    val start = posToCost[Position(y, i)]!!
                    val end = posToCost[Position(y, i + 2)]!!
                    // always needs to take two steps for the shortcut so -2
                    val shortcutCost = abs(end - start) - 2
                    shortcuts.add(shortcutCost)
                }
            }
        }

//        shortcuts.groupBy { it }
//            .toSortedMap()
//            .forEach {
//            println("${it.value.size} cheats that save ${it.key} picos")
//        }

        return shortcuts.filter {
            it >= 100
        }.size

        return -1
    }

    fun part2(input: List<String>): Int {

        return -1
    }

    val testInput = readInput("Day${dayNr}_test")
    val input = readInput("Day${dayNr}")

    check(part1(testInput) == 0)
    part1(input).println()

	check(part2(testInput) == -1)
    part2(input).println()
}
