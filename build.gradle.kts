plugins {
    id("java")
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.flywaydb.flyway") version "11.8.1"
    // id("com.github.spotbugs") version "6.4.7"
    id("org.graalvm.buildtools.native") version "0.11.1"
    id("com.diffplug.spotless") version "8.1.0"
  }

group = "io.github.clarenced"
version = "1.1.7"

repositories {
    mavenCentral()
}

tasks.bootJar {
    layered {
        enabled.set(true)
    }
}

tasks.jar {
    enabled = false
}

/*
tasks.named<BootBuildImage>("bootBuildImage") {
    imagePlatform.set("linux/arm64")
    val nativeArgs = listOf(
        "-march=compatibility",
        "--gc=serial",
        "-R:MaxHeapSize=256m",
        "-O3",
        "-J-Xmx8g",
        "-J-XX:MaxRAMPercentage=85.0",
    ).joinToString(" ")

    environment = mapOf(
        "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to nativeArgs,
        "BP_HEALTH_CHECKER_ENABLED" to "true",
    )

    docker {
        host.set("unix:///var/run/docker.sock")
        bindHostToBuilder.set(true)
    }
}*/




dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // PostgreSQL et Flyway
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")


    // Spring Boot DevTools for development
    implementation("org.springframework.boot:spring-boot-devtools")

    // Testing dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // TestContainers
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.21.0"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")

}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    useJUnitPlatform {
        includeTags("integration")
    }
}

tasks.named("check") {
  dependsOn("integrationTest")
}

spotless {

  java {
    importOrder()
    removeUnusedImports()
    // forbidWildcardImports()
    formatAnnotations()
    targetExclude("**/build/**/*.java")
  }
}
