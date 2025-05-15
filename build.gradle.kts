plugins {
    id("java")
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.flywaydb.flyway") version "11.8.1"
}

group = "io.github.clarenced"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

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
