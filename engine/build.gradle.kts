
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.SARIF
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.openapitools.generator.gradle.plugin.tasks.ValidateTask
import java.util.Locale

plugins {
  id("com.google.devtools.ksp") version "1.9.21-1.0.15"
  id("io.gitlab.arturbosch.detekt") version "1.23.7"
  id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
  id("io.ktor.plugin") version "2.3.7"
  id("org.openapi.generator") version "7.2.0"
  kotlin("jvm") version "1.9.21"
  kotlin("plugin.serialization") version "1.9.21"
  application
}

group = "chronos"
version = "0.0.1-SNAPSHOT"

application {
  mainClass.set("chronos.engine.ChronosApplicationKt")
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  manifest {
    attributes["Main-Class"] = "chronos.engine.ChronosApplicationKt"
  }
}

ktor {
  fatJar {
    archiveFileName.set("fat.jar")
  }
}

sourceSets {
  getByName("main") {
    kotlin {
      srcDir("${layout.buildDirectory.dir("./generated/openapi/src/main/kotlin").get().asFile}")
    }
  }
}

repositories {
  mavenCentral()
}

val versions =
  mapOf(
    "arrow" to "1.2.0",
    "slf4j" to "2.0.11",
    "logback" to "1.4.14",
    "mockk" to "1.13.9",
    "ktor" to "2.3.7",
    "exposed" to "0.46.0",
    "reactiveStreams" to "1.0.4",
    "kotlinxReactor" to "1.7.3",
    "koin" to "3.5.3",
    "koinKsp" to "1.3.0",
    "openApiTools" to "6.3.0",
    "jda" to "5.2.0",
    "jdaKtx" to "0.12.0",
  )

dependencies {
  // KOTLIN
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

  // ARROW
  implementation("io.arrow-kt:arrow-core:${versions["arrow"]}")
  implementation("io.arrow-kt:arrow-fx-coroutines:${versions["arrow"]}")
  implementation("io.arrow-kt:arrow-optics:${versions["arrow"]}")

  // SLF4J & LOGBACK
  implementation("org.slf4j:slf4j-api:${versions["slf4j"]}")
  implementation("ch.qos.logback:logback-classic:${versions["logback"]}")
  implementation("ch.qos.logback:logback-core:${versions["logback"]}")

  // Ktor
  implementation("io.ktor:ktor-server-core:${versions["ktor"]}")
  implementation("io.ktor:ktor-server-netty:${versions["ktor"]}")
  implementation("io.ktor:ktor-server-resources:${versions["ktor"]}")
  implementation("io.ktor:ktor-server-content-negotiation:${versions["ktor"]}")
  implementation("io.ktor:ktor-server-cors:${versions["ktor"]}")
  implementation("io.ktor:ktor-client-core:${versions["ktor"]}")
  implementation("io.ktor:ktor-client-okhttp:${versions["ktor"]}")
  implementation("io.ktor:ktor-client-okhttp-jvm:${versions["ktor"]}")
  implementation("io.ktor:ktor-client-logging:${versions["ktor"]}")
  implementation("io.ktor:ktor-client-content-negotiation:${versions["ktor"]}")
  implementation("io.ktor:ktor-client-encoding:${versions["ktor"]}")
  implementation("io.ktor:ktor-serialization-kotlinx-json:${versions["ktor"]}")
  implementation("io.ktor:ktor-serialization-gson:${versions["ktor"]}")
  implementation("io.ktor:ktor-server-data-conversion:${versions["ktor"]}")
  implementation("io.ktor:ktor-server-auto-head-response:${versions["ktor"]}")
  implementation("io.ktor:ktor-server-rate-limit:${versions["ktor"]}")

  // JDA
  implementation("net.dv8tion:JDA:${versions["jda"]}")
  implementation("club.minnced:jda-ktx:${versions["jdaKtx"]}")

  // EXPOSED
  implementation("org.jetbrains.exposed:exposed-core:${versions["exposed"]}")
  implementation("org.jetbrains.exposed:exposed-crypt:${versions["exposed"]}")
  implementation("org.jetbrains.exposed:exposed-dao:${versions["exposed"]}")
  implementation("org.jetbrains.exposed:exposed-jdbc:${versions["exposed"]}")
  implementation("org.jetbrains.exposed:exposed-java-time:${versions["exposed"]}")
  implementation("org.jetbrains.exposed:exposed-json:${versions["exposed"]}")
  implementation("org.jetbrains.exposed:exposed-money:${versions["exposed"]}")

  // REACTOR
  runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${versions["kotlinxReactor"]}")

  // KOIN
  implementation("io.insert-koin:koin-core:${versions["koin"]}")
  implementation("io.insert-koin:koin-ktor:${versions["koin"]}")
  implementation("io.insert-koin:koin-annotations:${versions["koinKsp"]}")
  implementation("io.insert-koin:koin-logger-slf4j:${versions["koin"]}")
  ksp("io.insert-koin:koin-ksp-compiler:${versions["koinKsp"]}")

  // OpenAPI
  implementation("org.openapitools:openapi-generator:${versions["openApiTools"]}")
  implementation("org.openapitools:openapi-generator-cli:${versions["openApiTools"]}")
  implementation("org.openapitools:openapi-generator-gradle-plugin:6.6.0")

  // TEST
  testImplementation("io.mockk:mockk:${versions["mockk"]}")
  testImplementation("io.ktor:ktor-server-test-host:${versions["ktor"]}")
}

detekt {
  buildUponDefaultConfig = true
  allRules = false
  config.setFrom("$projectDir/config/detekt.yml")
  baseline = file("$projectDir/config/baseline.xml")
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

tasks {
  withType<Detekt>().configureEach {
    jvmTarget = JavaVersion.VERSION_17.toString()
    reports {
      html.required.set(true)
      sarif.required.set(true)
    }
  }

  withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }

  withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs += "-Xjsr305=strict"
      jvmTarget = JavaVersion.VERSION_17.toString()
    }
  }

  withType<Test>().configureEach {
    useJUnitPlatform()
    workingDir = project.projectDir
  }

  val openApiList =
    File("$rootDir/src/main/resources/api/")
      .walkTopDown()
      .filter { it.isFile && (it.extension == "yaml" || it.extension == "yml") }
      .toList()

  fun String.capitalize(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
  }

  fun registerOpenApiTask(file: File) {
    val apiName = file.nameWithoutExtension.capitalize()

    register<GenerateTask>("openApiGenerate$apiName") {
      generatorName.set("kotlin")
      httpUserAgent.set("Chronos-API")
      inputSpec.set(file.absolutePath)
      outputDir.set(layout.buildDirectory.dir("./generated/openapi").get().toString())
      apiPackage.set("org.openapi.$apiName.api")
      invokerPackage.set("org.openapi.$apiName.invoker")
      modelPackage.set("org.openapi.$apiName.model")
      templateDir.set("$projectDir/src/main/resources/api/templates")
      modelNameSuffix.set("DTO")
      apiNameSuffix.set("OpenApi")
      cleanupOutput.set(false)
      validateSpec.set(true)
      enablePostProcessFile.set(true)
      generateAliasAsModel.set(true)
      configOptions.set(
        mapOf(
          "delegatePattern" to "true",
          "dateLibrary" to "java8-localdatetime",
          "library" to "jvm-ktor",
          "serializableModel" to "true",
          "serializationLibrary" to "gson",
          "useBeanValidation" to "true",
        ),
      )
    }

    register<ValidateTask>("openApiValidate$apiName") {
      inputSpec.set(file.absolutePath)
      recommend = true
    }
  }

  openApiList.forEach { registerOpenApiTask(it) }

  // Register a task to generate all clients
  register<Task>("openApiGenerateAll") {
    openApiList.forEach {
      dependsOn("openApiGenerate${it.nameWithoutExtension.capitalize()}")
    }
  }

  // Register a task to validate all clients
  register<Task>("openApiValidateAll") {
    openApiList.forEach {
      dependsOn("openApiValidate${it.nameWithoutExtension.capitalize()}")
    }
  }
}
