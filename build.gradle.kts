plugins {
	kotlin("jvm") version "2.0.21"
}

sourceSets {
	main {
		kotlin.srcDir("src")
	}
}
dependencies {
	implementation("org.jetbrains.kotlinx:kandy-lets-plot:0.7.0")
}
tasks {
	wrapper {
		gradleVersion = "8.11"
	}
}
