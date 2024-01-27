
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.SARIF
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.openapitools.generator.gradle.plugin.tasks.ValidateTask

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

  object Koin : Dependency("3.5.3")

  object KoinKsp : Dependency("1.3.0")

  object OpenApiTools : Dependency("7.2.0")

  override fun toString(): String = version
}

plugins {
  id("com.google.devtools.ksp") version "1.9.21-1.0.15"
  id("io.gitlab.arturbosch.detekt") version "1.23.4" // ./gradlaw detekt
  id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
  id("io.ktor.plugin") version "2.3.7"
  id("org.openapi.generator") version "7.2.0"
  kotlin("jvm") version "1.9.21"
  kotlin("plugin.serialization") version "1.9.21"
}

group = "chronos"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
}

sourceSets {
  getByName("main") {
    kotlin {
      srcDir("$buildDir/generated/openapi/src/main/kotlin")
    }
  }
}

repositories {
  mavenCentral()
}

dependencies { //    KOTLIN
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2") //    ARROW
  implementation("io.arrow-kt:arrow-core:${Dependency.Arrow}")
  implementation("io.arrow-kt:arrow-fx-coroutines:${Dependency.Arrow}")
  implementation("io.arrow-kt:arrow-optics:${Dependency.Arrow}") //    SLF4J
  implementation("org.slf4j:slf4j-api:${Dependency.Slf4j}") //    LOGBACK
  implementation("ch.qos.logback:logback-classic:${Dependency.Logback}")
  implementation("ch.qos.logback:logback-core:${Dependency.Logback}") //    Ktor
  implementation("io.ktor:ktor-server-core")
  implementation("io.ktor:ktor-server-netty")
  implementation("io.ktor:ktor-server-resources")
  implementation("io.ktor:ktor-server-content-negotiation")
  implementation("io.ktor:ktor-server-cors")
  implementation("io.ktor:ktor-server-resources")

  implementation("io.ktor:ktor-client-core")
  implementation("io.ktor:ktor-client-okhttp")
  implementation("io.ktor:ktor-client-okhttp-jvm")
  implementation("io.ktor:ktor-client-logging")
  implementation("io.ktor:ktor-client-content-negotiation")
  implementation("io.ktor:ktor-client-encoding")
  implementation("io.ktor:ktor-client-logging-jvm")
  implementation("io.ktor:ktor-client-encoding")
  implementation("io.ktor:ktor-serialization-kotlinx-json")
  implementation("io.ktor:ktor-serialization-gson")

  testImplementation("io.ktor:ktor-server-test-host") //    EXPOSED
  implementation("org.jetbrains.exposed:exposed-core:${Dependency.Exposed}")
  implementation("org.jetbrains.exposed:exposed-crypt:${Dependency.Exposed}")
  implementation("org.jetbrains.exposed:exposed-dao:${Dependency.Exposed}")
  implementation("org.jetbrains.exposed:exposed-jdbc:${Dependency.Exposed}")
  implementation("org.jetbrains.exposed:exposed-java-time:${Dependency.Exposed}") //    implementation("com.h2database:h2")
  implementation("org.jetbrains.exposed:exposed-json:${Dependency.Exposed}")
  implementation("org.jetbrains.exposed:exposed-money:${Dependency.Exposed}")
  implementation("io.ktor:ktor-server-cors-jvm:2.3.7") //    REACTIVE STREAMS
  //    Needed at runtime for suspended @Controller endpoints
  runtimeOnly("org.reactivestreams:reactive-streams:${Dependency.ReactiveStreams}")
  runtimeOnly("org.reactivestreams:reactive-streams-tck:${Dependency.ReactiveStreams}")
  runtimeOnly("org.reactivestreams:reactive-streams-tck-flow:${Dependency.ReactiveStreams}") //      REACTOR
  //    Needed at runtime for suspended @Controller endpoints
  runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Dependency.KotlinxReactor}") //    KOIN
  implementation("io.insert-koin:koin-core:${Dependency.Koin}")
  implementation("io.insert-koin:koin-ktor:${Dependency.Koin}")
  implementation("io.insert-koin:koin-annotations:${Dependency.KoinKsp}")
  implementation("io.insert-koin:koin-logger-slf4j:${Dependency.Koin}")
  testImplementation("io.ktor:ktor-server-test-host:${Dependency.Ktor}")
  testImplementation("io.insert-koin:koin-test:${Dependency.Koin}")
  testImplementation("io.insert-koin:koin-test-junit5:${Dependency.Koin}")
  ksp("io.insert-koin:koin-ksp-compiler:${Dependency.KoinKsp}") //    Open API Generate
  implementation("org.openapitools:openapi-generator:${Dependency.OpenApiTools}")
  implementation("org.openapitools:openapi-generator-cli:${Dependency.OpenApiTools}")
  implementation("org.openapitools:openapi-generator-gradle-plugin:6.6.0") //    TEST
  testImplementation("io.mockk:mockk:${Dependency.Mockk}")
}

detekt {
  buildUponDefaultConfig = true // preconfigure defaults
  allRules = false // activate all available (even unstable) rules.
  config.setFrom("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
  baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
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
    jvmTarget = "21"
    reports {
      html.required.set(true) // observe findings in your browser with structure and code snippets
      sarif.required.set(
        true,
      ) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
    }
  }

  withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "21"
  }

  withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs += "-Xjsr305=strict"
      jvmTarget = "21"
    }
  }

  withType<Test>().configureEach {
    useJUnitPlatform()
    workingDir = project.projectDir
  }

  // Iteration by swagger file root folder and save into swaggerList variable
  val openApiList = mutableListOf<File>()
  val dir = File("$rootDir/src/main/resources/api/")
  dir.walkTopDown().filter { it.isFile && (it.extension == "yaml" || it.extension == "yml") }.toCollection(openApiList)

  // Register a task to generate all clients
  register<Task>("openApiGenerateAll") {
    openApiList.forEach {
      val task = "openApiGenerate" + it.nameWithoutExtension.capitalize()
      dependsOn(task)
    }
  }
  // Register a task to validate all clients
  register<Task>("openApiValidateAll") {
    openApiList.forEach {
      val task = "openApiValidate" + it.nameWithoutExtension.capitalize()
      dependsOn(task)
    }
  }

  // Iterate on all swagger files and generate a task for each one with the nomenclature openApiGenerate + swagger name
  openApiList.forEach {
    println(it.path)
    val apiName = it.nameWithoutExtension

    register<GenerateTask>("openApiGenerate" + apiName.capitalize()) {
      generatorName.set("kotlin")
      httpUserAgent.set("Chronos-API")
      inputSpec.set("$projectDir/src/main/resources/api/$apiName.yml")
      outputDir.set("$buildDir/generated/openapi")
      apiPackage.set("org.openapi.$apiName.api")
      invokerPackage.set("org.openapi.$apiName.invoker")
      modelPackage.set("org.openapi.$apiName.model")
      templateDir.set("$projectDir/src/main/resources/api/templates")
      modelNameSuffix.set("DTO")
      apiNameSuffix.set("OpenApi")
      cleanupOutput.set(true)
      validateSpec.set(true)
      enablePostProcessFile.set(true)
      // 	generateApiTests.set(true)
      // 	generateModelTests.set(true)
      // For more details, refer: https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/jaxrs-spec.md
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
    register<ValidateTask>("openApiValidate" + apiName.capitalize()) {
      inputSpec.set("$projectDir/src/main/resources/api/$apiName.yml")
      recommend = true
    }
  }
}
