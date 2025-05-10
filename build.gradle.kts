plugins {
    id("java")
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.flywaydb.flyway") version "9.22.3"
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
    implementation("org.springframework.boot:spring-boot-starter-jpa")

    // PostgreSQL et Flyway
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("org.flywaydb:flyway-core")


    // Spring Boot DevTools for development
    implementation("org.springframework.boot:spring-boot-devtools")

    // Testing dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // TestContainers
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.19.3"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers")

}

tasks.test {
    useJUnitPlatform()
}
