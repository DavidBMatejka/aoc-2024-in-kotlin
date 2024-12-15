import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.layers.points
import org.jetbrains.kotlinx.kandy.letsplot.settings.Symbol

fun main() {
	data class Robot(var x: Int, var y: Int, val vx: Int, val vy: Int) {
		fun move(steps: Int, w: Int, h: Int) {
			x += vx * steps
			y += vy * steps
			x = Math.floorMod(x, w)
			y = Math.floorMod(y, h)
		}

		fun getQuadrant(w: Int, h: Int): Int {
			val mx = w / 2
			val my = h / 2
			when {
				x > mx -> {
					when {
						y < my -> return 0
						y > my -> return 3
					}
				}

				x < mx -> {
					when {
						y < my -> return 1
						y > my -> return 2
					}
				}
			}
			return -1
		}
	}

	fun parseInput(input: List<String>): MutableList<Robot> {
		val robots = mutableListOf<Robot>()

		input.forEach { line ->
			val (x, y) = line.split(" ").first().substringAfter("=").split(",")
				.map { it.toInt() }
			val (vx, vy) = line.split(" ").last().substringAfter("=").split(",")
				.map { it.toInt() }
			robots.add(Robot(x, y, vx, vy))
		}

		return robots
	}

	fun part1(input: List<String>, w: Int, h: Int): Int {
		val robots = parseInput(input)
		robots.forEach {
			it.move(100, w, h)
		}

		val quadrants = MutableList(4) { 0 }
		robots.forEach { robot ->
			val quad = robot.getQuadrant(w, h)
			if (quad != -1) quadrants[quad] += 1
		}

		return quadrants.fold(1) { acc, e ->
			acc * e
		}
	}

	fun createPNG(input: List<String>, step: Int, w: Int, h: Int) {
		val robots = parseInput(input)
		val xs = mutableListOf<Int>()
		val ys = mutableListOf<Int>()
		robots.forEach {
			it.move(step, w, h)
			xs.add(it.x)
			ys.add(it.y)
		}
		plot {
			points {
				x(xs)
				y(ys)
				symbol = Symbol.SQUARE
			}
		}.save("${step}_tree.png")
	}

	fun part2(input: List<String>, w: Int, h: Int): Int {
		val robots = parseInput(input)

		val stepToFactor = mutableMapOf<Int, Int>()

		(1..10000).forEach { i ->
			robots.forEach {
				it.move(1, w, h)
			}

			val quadrants = MutableList(4) { 0 }
			robots.forEach { robot ->
				val quad = robot.getQuadrant(w, h)
				if (quad != -1) quadrants[quad] += 1
			}

			val factor = quadrants.fold(1) { acc, e ->
				acc * e
			}
			stepToFactor[i] = factor
		}

		val erg = stepToFactor.minBy { it.value }
		// just for fun also create a png to see the tree in all its glory
		createPNG(input, erg.key, w, h)
		return erg.key
	}

	val testInput = readInput("Day14_test")
	val input = readInput("Day14")

	check(part1(testInput, 11, 7) == 12)
	part1(input, 101, 103).println()
	part2(input, 101, 103).println()
}
