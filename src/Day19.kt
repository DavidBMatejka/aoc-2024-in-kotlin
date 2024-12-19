fun main() {
    val dayNr = 19

    fun part1(input: List<String>): Int {
        val mem = mutableMapOf<String, Boolean>()
        var availablePatterns = listOf<String>()

        fun parseInput(input: List<String>): List<String> {
            availablePatterns = input[0].split(", ")
            availablePatterns = availablePatterns.sortedByDescending { it.length }
            val desiredDesigns = input.dropWhile { it.isNotEmpty() }.drop(1)
            return desiredDesigns
        }

        fun checkTowel(pattern: String): Boolean {
            if (pattern == "") return true
            if (pattern in mem) return mem[pattern]!!

            mem[pattern] = false
            for (w in availablePatterns) {
                val length = w.length
                if (length <= pattern.length) {
                    val start = pattern.substring(0, length)
                    val rest = pattern.substring(length)
                    if (start == w && checkTowel(rest)) {
                        mem[pattern] = true
                    }
                }
            }
            return mem[pattern]!!
        }
        val desiredDesigns = parseInput(input)

        var sum = 0
        desiredDesigns.forEach { design ->
            if (checkTowel(design) == true) {
                sum++
            }
        }

        return sum
    }


    fun part2(input: List<String>): Int {

        return -1
    }

    val testInput = readInput("Day${dayNr}_test")
    val input = readInput("Day${dayNr}")

    check(part1(testInput) == 6)
    part1(input).println()
//
//	check(part2(testInput) == -1)
//    part2(input).println()
}
