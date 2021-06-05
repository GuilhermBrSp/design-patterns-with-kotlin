import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.blackhawk"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation("org.amshove.kluent:kluent:1.65")
    testImplementation("org.spockframework:spock-core:2.0-groovy-3.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "MainKt"
}