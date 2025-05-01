val kotlin_version: String by project
val ktor_version: String by project
val koin_version: String by project
val exposed_version: String by project
val postgres_version: String by project
val hikari_version: String by project
val flyway_version: String by project
val jasypt_version: String by project
val logback_version: String by project
val mockk_version: String by project
val kotlinx_html_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "3.0.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

group = "ru.pkozlov"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

jib {
    to {
        image = "brackets-back"
        tags = setOf("1.0.0")
    }
    from {
        image = "eclipse-temurin:17-jre"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    container {
        creationTime.set("USE_CURRENT_TIMESTAMP")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    /** Koin */
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    /** Ktor */
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-status-pages-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-sessions")
    implementation("io.ktor:ktor-server-auth")

    /** JetBrains */
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinx_html_version")

    /** Database */
    implementation("com.zaxxer:HikariCP:$hikari_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-json:$exposed_version")
    implementation("org.flywaydb:flyway-database-postgresql:$flyway_version")

    /** Jasypt */
    implementation("org.jasypt:jasypt:$jasypt_version")

    /** Logging */
    implementation("ch.qos.logback:logback-classic:$logback_version")

    /** Tests */
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit4:$koin_version")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("io.ktor:ktor-client-content-negotiation-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.mockk:mockk:$mockk_version")
}
