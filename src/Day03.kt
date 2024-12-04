fun main() {
    fun part1(input: List<String>): Int {
        val regex = Regex("mul\\(([0-9]+),([0-9]+)\\)")
        val matches = regex.findAll(input.joinToString())
        return matches.fold(0) { sum, e ->
            sum + (e.groupValues[1].toInt() * e.groupValues[2].toInt())
        }
    }

    fun getBeforeDont(line: String): Pair<String, String> {
        val erg = line.substringBefore("don't()")
        val rest = line.substringAfter("don't()")
        return erg to rest
    }

    fun removeUntilDo(line: String): String {
        val erg = line.substringAfter("do()")
        return erg
    }

    fun removeInactiveParts(line: String): String {
        var (erg, rest) = getBeforeDont(line)
        if (rest == erg) return erg
        erg += removeUntilDo(rest)
        return erg
    }

    fun part2(input: List<String>): Int {
        var erg = input.joinToString()
        var sum = 0
        while (true) {
            val prev = erg
            erg = removeInactiveParts(erg)
            if (erg == prev) break
        }
        sum += part1(listOf(erg))
        return sum
    }

    fun part2Regex(input: List<String>): Int {
        var text = input.joinToString()
        text = "do()" + text + "don't()"
        val matches = Regex("do\\(\\)(.*?)don't\\(\\)").findAll(text)
        return matches.fold(0) { sum, e ->
            sum + part1(e.groupValues.drop(1))
        }
    }

    check(part1(listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")) == 161)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)
    check(part2Regex(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
    part2Regex(input).println()
}