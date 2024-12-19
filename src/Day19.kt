fun main() {
    val dayNr = 19

    fun parseInput(input: List<String>): Pair<List<String>, List<String>> {
        val rules = input[0].split(", ")
        val designs = input.dropWhile { it.isNotEmpty() }.drop(1)
        return Pair(rules, designs)
    }

    fun part1(input: List<String>): Int {
        val (rules, designs) = parseInput(input)

        val mem = mutableMapOf<String, Boolean>()
        fun checkTowel(pattern: String, rules: List<String>): Boolean {
            if (pattern == "") return true
            if (pattern in mem) return mem[pattern]!!

            mem[pattern] = false
            for (w in rules) {
                val length = w.length
                if (length <= pattern.length) {
                    val start = pattern.substring(0, length)
                    val rest = pattern.substring(length)
                    if (start == w && checkTowel(rest, rules)) {
                        mem[pattern] = true
                    }
                }
            }
            return mem[pattern]!!
        }

        var sum = 0
        designs.forEach { design ->
            if (checkTowel(design, rules) == true) {
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

//    check(part1(testInput) == 6)
//    part1(input).println()

    part2(testInput)

//	check(part2(testInput) == -1)
//    part2(input).println()
}
