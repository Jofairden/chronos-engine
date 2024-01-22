import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.SARIF

sealed class Dependency(
    private val version: String,
) {
    object Arrow : Dependency("1.2.0")

    object Slf4j : Dependency("2.0.11")

    object Logback : Dependency("1.4.14")

    object Mockk : Dependency("1.13.9")

    object Ktor : Dependency("2.3.7")

    object Exposed : Dependency("0.46.0")

    object ReactiveStreams : Dependency("1.0.4")

    object KotlinxReactor : Dependency("1.7.3")

    override fun toString(): String = version
}

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
    id("io.gitlab.arturbosch.detekt") version "1.23.4" // ./gradlaw detekt
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
//    kotlin("plugin.serialization") version "1.7.3"
}

group = "chronos"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
//    SPRING
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
//    KOTLIN
    implementation("org.jetbrains.kotlin:kotlin-reflect")
//    ARROW
    implementation("io.arrow-kt:arrow-core:${Dependency.Arrow}")
    implementation("io.arrow-kt:arrow-fx-coroutines:${Dependency.Arrow}")
    implementation("io.arrow-kt:arrow-optics:${Dependency.Arrow}")
//    SLF4J
    implementation("org.slf4j:slf4j-api:${Dependency.Slf4j}")
//    LOGBACK
    implementation("ch.qos.logback:logback-classic:${Dependency.Logback}")
    implementation("ch.qos.logback:logback-core:${Dependency.Logback}")
//    Ktor
    implementation("io.ktor:ktor-client-core:${Dependency.Ktor}")
    implementation("io.ktor:ktor-client-okhttp:${Dependency.Ktor}")
    implementation("io.ktor:ktor-client-okhttp-jvm:${Dependency.Ktor}")
    implementation("io.ktor:ktor-client-logging:${Dependency.Ktor}")
    implementation("io.ktor:ktor-client-content-negotiation:${Dependency.Ktor}")
    implementation("io.ktor:ktor-client-encoding:${Dependency.Ktor}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${Dependency.Ktor}")
    implementation("io.ktor:ktor-client-logging-jvm:${Dependency.Ktor}")
    implementation("io.ktor:ktor-serialization-gson:${Dependency.Ktor}")
//     CACHE
    implementation("io.ktor:ktor-client-encoding:2.3.7")
//    EXPOSED
    implementation("org.jetbrains.exposed:exposed-core:${Dependency.Exposed}")
    implementation("org.jetbrains.exposed:exposed-crypt:${Dependency.Exposed}")
    implementation("org.jetbrains.exposed:exposed-dao:${Dependency.Exposed}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${Dependency.Exposed}")
    implementation("org.jetbrains.exposed:exposed-java-time:${Dependency.Exposed}")
    implementation("com.h2database:h2")
    implementation("org.jetbrains.exposed:exposed-json:${Dependency.Exposed}")
    implementation("org.jetbrains.exposed:exposed-money:${Dependency.Exposed}")
    implementation("org.jetbrains.exposed:spring-transaction:${Dependency.Exposed}")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:${Dependency.Exposed}")
//    REACTIVE STREAMS
//    Needed at runtime for suspended @Controller endpoints
    runtimeOnly("org.reactivestreams:reactive-streams:${Dependency.ReactiveStreams}")
    runtimeOnly("org.reactivestreams:reactive-streams-tck:${Dependency.ReactiveStreams}")
    runtimeOnly("org.reactivestreams:reactive-streams-tck-flow:${Dependency.ReactiveStreams}")
//      REACTOR
//    Needed at runtime for suspended @Controller endpoints
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Dependency.KotlinxReactor}")
//    TEST
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("io.mockk:mockk:${Dependency.Mockk}")
    testImplementation("io.ktor:ktor-server-test-host:${Dependency.Ktor}")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        sarif.required.set(
            true,
        ) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
    }
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)

    reporters {
        reporter(HTML)
        reporter(SARIF)
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "21"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "21"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
