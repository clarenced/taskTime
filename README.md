# Functional Core, Imperative Shell Analysis

> **DISCLAIMER**: This project is simply an experimentation to learn "**Functional Core / Imperative Shell**" architecture. 
> It should not be considered a reference implementation 
> but rather as an exploration ground for concepts and practices related to this architectural approach.
> It's not perfect but a work in progress. 

## Current Architecture

The current architecture of the TaskTime application follows the Functional Core, Imperative Shell pattern quite well. Here's a breakdown of the components:

### Functional Core (Pure Functions, No Side Effects)

1. **Domain Objects**:
   - `Task` - Immutable record representing a task
   - `TaskStatus` - Enum representing task states
   - `Error` - Immutable record representing validation errors

2. **Validators and Checkers**:
   - `TaskValidator` - Pure functions for validating tasks
   - `TaskExistenceChecker` - Pure functions for checking task existence

3. **Mappers and Factories**:
   - `TaskMapper` - Pure functions for converting between domain objects and DTOs
   - `ErrorFactory` - Pure functions for creating error objects

4. **Business Logic**:
   - `TaskUpdator` - Pure functions for updating tasks

5. **Utilities**:
   - `Result` - Immutable class for representing operation results (success or error)

### Imperative Shell (Side Effects, I/O, State)

1. **API Layer**:
   - `TaskTimeApi` - Spring REST controller handling HTTP requests/responses

2. **Coordination**:
   - `TaskCoordinator` - Orchestrates operations between components

3. **Persistence**:
   - `TaskRepository` - Manages task storage and retrieval