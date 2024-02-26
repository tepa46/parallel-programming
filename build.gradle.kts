plugins {
    kotlin("jvm") version "1.9.22"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:atomicfu:0.23.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")

    testImplementation("org.jetbrains.kotlinx:lincheck:2.26")
}

tasks.test {
    useJUnitPlatform()
}
