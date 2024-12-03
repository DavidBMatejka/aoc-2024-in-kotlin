fun main() {
    fun part1(input: List<String>): Int {
        val regex = Regex("mul\\(([0-9]+),([0-9]+)\\)")
        var sum = 0
        for (line in input) {
            val matches = regex.findAll(line)
            matches.forEach {
                sum += it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }
        }

        return sum
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
        var text = ""
        for (line in input) {
            text += line
        }

        var sum = 0
        var erg = text
        while (true) {
            val prev = erg
            erg = removeInactiveParts(erg)
            if (erg == prev) break
        }
        sum += part1(listOf(erg))
        return sum
    }

    check(part1(listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")) == 161)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 96)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)


    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}