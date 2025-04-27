package io.github.clarenced.tasktime.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskValidatorTest {
    private static final String VALID_TITLE = "Test Task";
    private static final String VALID_DESCRIPTION = "Test Description";

    @Test
    @DisplayName("Should validate task with valid data")
    void validateTaskWithValidData() {
        TaskTimeApi.CreateTaskDto createTaskDto = createTask(VALID_TITLE, VALID_DESCRIPTION);
        Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> result = TaskValidator.validateTask(createTaskDto);

        assertValidationSuccess(result, createTaskDto);
    }

    @Nested
    @DisplayName("Title validation tests")
    class TitleValidationTests {
        @Test
        @DisplayName("Should return error when title is empty")
        void validateEmptyTitle() {
            var result = validateTask("", VALID_DESCRIPTION);
            assertValidationError(result, "title", "title is empty");
        }

        @Test
        @DisplayName("Should return error when title is too long")
        void validateTitleTooLong() {
            var longTitle = "Create a new Unit test".repeat(35);
            var result = validateTask(longTitle, VALID_DESCRIPTION);
            assertValidationError(result, "title", "title has more than 30 characters");
        }

        @Test
        @DisplayName("Should return error when title is too short")
        void validateTitleTooShort() {
            var result = validateTask("Test", VALID_DESCRIPTION);
            assertValidationError(result, "title", "title must have at least 5 characters");
        }
    }

    @Nested
    @DisplayName("Description validation tests")
    class DescriptionValidationTests {
        @Test
        @DisplayName("Should return error when description is empty")
        void validateEmptyDescription() {
            var result = validateTask(VALID_TITLE, "");
            assertValidationError(result, "description", "description is empty");
        }

        @Test
        @DisplayName("Should return error when description is too long")
        void validateDescriptionTooLong() {
            var longDescription = "Test Description".repeat(105);
            var result = validateTask(VALID_TITLE, longDescription);
            assertValidationError(result, "description", "description has more than 300 characters");
        }

        @Test
        @DisplayName("Should return error when description is too short")
        void validateDescriptionTooShort() {
            var result = validateTask(VALID_TITLE, "Test");
            assertValidationError(result, "description", "description must have at least 5 characters");
        }
    }

    private Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> validateTask(String title, String description) {
        return TaskValidator.validateTask(createTask(title, description));
    }

    private TaskTimeApi.CreateTaskDto createTask(String title, String description) {
        return new TaskTimeApi.CreateTaskDto(title, description);
    }

    private void assertValidationSuccess(Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> result,
                                         TaskTimeApi.CreateTaskDto expectedDto) {
        assertTrue(result.isSuccess());
        assertEquals(expectedDto, result.getSuccess());
    }

    private void assertValidationError(Result<TaskTimeApi.CreateTaskDto, TaskTimeApi.ErrorDto> result,
                                       String expectedField, String expectedMessage) {
        assertTrue(result.isError());
        assertEquals(expectedField, result.getError().field());
        assertEquals(expectedMessage, result.getError().message());
    }

}
