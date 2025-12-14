# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

TaskTime is a Spring Boot 3.5 application exploring the **Functional Core / Imperative Shell** architecture pattern. It's a task management application with a PostgreSQL backend and optional GraalVM native image support.

## Build Commands

```bash
# Build the project
./gradlew build

# Run tests (unit tests)
./gradlew test

# Run integration tests (uses TestContainers with PostgreSQL)
./gradlew integrationTest

# Run a single test class
./gradlew test --tests "io.github.clarenced.tasktime.tasks.TaskTest"

# Run a single test method
./gradlew test --tests "io.github.clarenced.tasktime.tasks.TaskTest.testMethodName"

# Run the application
./gradlew bootRun

# Format code with Spotless
./gradlew spotlessApply

# Check code formatting
./gradlew spotlessCheck

# Build native image (requires GraalVM)
./gradlew nativeCompile

# Build container image with native executable
./gradlew bootBuildImage
```

## Architecture

This project follows the **Functional Core / Imperative Shell** pattern:

### Functional Core (Pure, No Side Effects)
- **Domain objects** (`tasks/domain/`): Immutable classes like `Task`, `TaskStatus`, `Error`
- **Business logic** (`tasks/application/TaskUpdator`): Pure functions for task operations
- **Result type** (`common/Result`): Functional error handling instead of exceptions, supports `map()`, `flatMap()`, `onSuccess()`, `onError()`
- Domain objects use factory methods (`Task.create()`) that return `Result<Task, Error>` for validation

### Imperative Shell (Side Effects, I/O)
- **API layer** (`tasks/api/TaskTimeApi`): REST controller handling HTTP
- **Web layer** (`tasks/web/`): Thymeleaf-based web UI
- **Coordination** (`tasks/application/TaskCoordinator`): Orchestrates between pure core and impure boundaries
- **Persistence** (`tasks/infrastructure/`): JPA repository adapters

### Key Patterns
- Validation is embedded in domain object creation via `Task.create()` returning `Result`
- `TaskCoordinator` is the service layer that bridges API/Web controllers with domain logic and repositories
- `TaskRepository` interface with `JpaTaskRepositoryAdapter` for production and `FakeTaskRepository` for testing

## Testing

- Unit tests: Test pure functions directly without mocking
- Integration tests: Tagged with `@Tag("integration")`, use TestContainers for PostgreSQL
- `WithPostgres` trait and `PostgreSqlTestConfiguration` provide shared container setup

## Database

- PostgreSQL with Flyway migrations in `src/main/resources/db/migration/`
- Test-specific migrations in `src/test/resources/db/migration/`
