fun main() {
    val dayNr = 22

    fun Long.mix(other: Long): Long = this.toLong().xor(other.toLong())
    fun Long.prune(): Long = this.toLong().mod(16777216L)
    fun Long.evolve(): Long {
        val s1 = this.mix(this * 64).prune()
        val s2 = s1.mix(s1 / 32).prune()
        return s2.mix(s2 * 2048).prune()
    }

    fun part1(input: List<String>): Long {
        var sum = 0L
        input.forEach { line ->
            var secret = line.toLong()
			(1..2000).forEach { secret = secret.evolve() }
            sum += secret
        }

        return sum
    }


    fun getSequences(input: List<String>): Set<String> {
        val sequences = mutableSetOf<String>()
        input.forEach { line ->
            val list = mutableListOf<Int>()
            var secret = line.toLong()
            (1..2000).forEach {
                list.add("$secret".last().digitToInt())
                secret = secret.evolve()
            }

            list.windowed(5).forEach {
                val sequence = it.zipWithNext().map {
                    val diff = it.second - it.first
                    diff
                }.joinToString(",")
                sequences.add(sequence)
            }
        }
        return sequences
    }

    fun getSequenceToBananas(input: List<String>): List<Map<String, Int>> {
        val listOfSequenceToBanana = mutableListOf<Map<String, Int>>()
        input.forEach { line ->
            val list = mutableListOf<Int>()
            var secret = line.toLong()
            (1..2000).forEach {
                list.add("$secret".last().digitToInt())
                secret = secret.evolve()
            }

            val sequenceToBanana = mutableMapOf<String, Int>()
            list.windowed(5).forEach {
                val sequence = it.zipWithNext().map {
                    val diff = it.second - it.first
                    diff
                }.joinToString(",")
                val bananas = it.last()
                if (sequence !in sequenceToBanana) {
                    sequenceToBanana[sequence] = bananas
                }
            }
            listOfSequenceToBanana.add(sequenceToBanana)
        }
        return listOfSequenceToBanana
    }

    fun part2(input: List<String>): Int {
        val sequences = getSequences(input)
        val listOfSequenceToBananas = getSequenceToBananas(input)

        val sequenceToSum = mutableMapOf<String, Int>()
        sequences.forEach { sequence ->
            var sum = 0
            listOfSequenceToBananas.forEach {
                sum += it[sequence] ?: 0
            }
            sequenceToSum[sequence] = sum
        }

		return sequenceToSum.toList().maxByOrNull { it.second }!!.second
    }

    val testInput = readInput("Day${dayNr}_test")
    val input = readInput("Day${dayNr}")

    check(part1(testInput) == 37327623L)
    part1(input).println()

	check(part2(listOf("1", "2", "3", "2024")) == 23)
    part2(input).println()
}