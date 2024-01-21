import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

sealed class versions(
    private val version : String
) {
    object Arrow : versions("1.2.0")
    object Slf4j : versions("2.0.11")
    object Logback : versions("1.4.14")
    object Mockk : versions("1.13.9")
    object Ktor : versions("2.3.7")
    object Exposed : versions("0.46.0" )
    object ReactiveStreams : versions("1.0.4" )
    object KotlinxReactor : versions("1.7.3" )

    override fun toString(): String = version
}

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
    id("io.gitlab.arturbosch.detekt") version "1.23.4"
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
    implementation("io.arrow-kt:arrow-core:${versions.Arrow}" )
    implementation("io.arrow-kt:arrow-fx-coroutines:${versions.Arrow}")
    implementation("io.arrow-kt:arrow-optics:${versions.Arrow}")
//    SLF4J
    implementation("org.slf4j:slf4j-api:${versions.Slf4j}")
//    LOGBACK
    implementation("ch.qos.logback:logback-classic:${versions.Logback}")
    implementation("ch.qos.logback:logback-core:${versions.Logback}")
//    Ktor
    implementation("io.ktor:ktor-client-core:${versions.Ktor}")
    implementation("io.ktor:ktor-client-okhttp:${versions.Ktor}")
    implementation("io.ktor:ktor-client-okhttp-jvm:${versions.Ktor}")
    implementation("io.ktor:ktor-client-logging:${versions.Ktor}")
    implementation("io.ktor:ktor-client-content-negotiation:${versions.Ktor}")
    implementation("io.ktor:ktor-client-encoding:${versions.Ktor}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${versions.Ktor}")
    implementation("io.ktor:ktor-client-logging-jvm:${versions.Ktor}")
    implementation("io.ktor:ktor-serialization-gson:${versions.Ktor}")
//     CACHE
    implementation("io.ktor:ktor-client-encoding:2.3.7")
//    EXPOSED
    implementation("org.jetbrains.exposed:exposed-core:${versions.Exposed}")
    implementation("org.jetbrains.exposed:exposed-crypt:${versions.Exposed}")
    implementation("org.jetbrains.exposed:exposed-dao:${versions.Exposed}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${versions.Exposed}")
    implementation("org.jetbrains.exposed:exposed-java-time:${versions.Exposed}")
    implementation("com.h2database:h2")
    implementation("org.jetbrains.exposed:exposed-json:${versions.Exposed}")
    implementation("org.jetbrains.exposed:exposed-money:${versions.Exposed}")
    implementation("org.jetbrains.exposed:spring-transaction:${versions.Exposed}")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:${versions.Exposed}")
//    REACTIVE STREAMS
//    Needed at runtime for suspended @Controller endpoints
    runtimeOnly("org.reactivestreams:reactive-streams:${versions.ReactiveStreams}")
    runtimeOnly("org.reactivestreams:reactive-streams-tck:${versions.ReactiveStreams}")
    runtimeOnly("org.reactivestreams:reactive-streams-tck-flow:${versions.ReactiveStreams}")
//      REACTOR
//    Needed at runtime for suspended @Controller endpoints
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${versions.KotlinxReactor}")
//    TEST
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("io.mockk:mockk:${versions.Mockk}")
    testImplementation("io.ktor:ktor-server-test-host:${versions.Ktor}")
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
        xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
        txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
        sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
        md.required.set(true) // simple Markdown format
    }
}
// Kotlin DSL
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
