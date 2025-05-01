package io.github.clarenced.tasktime.tasks;

import io.github.clarenced.tasktime.common.Result;
import io.github.clarenced.tasktime.tasks.domain.Error;
import io.github.clarenced.tasktime.tasks.domain.Task;
import io.github.clarenced.tasktime.tasks.domain.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task should")
class TaskTest {
    private static final String VALID_TITLE = "Test Task";
    private static final String VALID_DESCRIPTION = "Test Description";

    @Test
    @DisplayName("Create a valid task successfully")
    void should_create_valid_task_successfully() {
        var result = Task.create(VALID_TITLE, VALID_DESCRIPTION, TaskStatus.TO_DO);

        assertTrue(result.isSuccess());
        var task = result.getSuccess();
        assertEquals(VALID_TITLE, task.getTitle());
        assertEquals(VALID_DESCRIPTION, task.getDescription());
        assertEquals(TaskStatus.TO_DO, task.getStatus());
    }

    @Nested
    @DisplayName("Title validation tests")
    class TitleValidationTests {
        @Test
        @DisplayName("Should return error when title is too long")
        void should_return_error_when_title_is_too_long() {
            var longTitle = "Create a new Unit test".repeat(35);
            var result = Task.create(longTitle, VALID_DESCRIPTION, TaskStatus.TO_DO);
            assertValidationError(result, "title", "title has more than 30 characters");
        }

        @Test
        @DisplayName("Should return error when title is empty")
        void should_return_error_when_title_is_empty() {
            var result = Task.create("", VALID_DESCRIPTION, TaskStatus.TO_DO);
            assertValidationError(result, "title", "title is empty");
        }

        @Test
        @DisplayName("Should return error when title is too short")
        void should_return_error_when_title_is_too_short() {
            var result = Task.create("Test", VALID_DESCRIPTION, TaskStatus.TO_DO);
            assertValidationError(result, "title", "title must have at least 5 characters");
        }
    }

    @Nested
    @DisplayName("Description validation tests")
    class DescriptionValidationTests {
        @Test
        @DisplayName("Should return error when description is empty")
        void should_return_error_when_description_is_empty() {
            var result = Task.create(VALID_TITLE, "", TaskStatus.TO_DO);
            assertValidationError(result, "description", "description is empty");
        }

        @Test
        @DisplayName("Should return error when description is too long")
        void should_return_error_when_description_is_too_long() {
            var longDescription = "Test Description".repeat(105);
            var result = Task.create(VALID_TITLE, longDescription, TaskStatus.TO_DO);
            assertValidationError(result, "description", "description has more than 300 characters");
        }

        @Test
        @DisplayName("Should return error when description is too short")
        void should_return_error_when_description_is_too_short() {
            var result = Task.create(VALID_TITLE, "Test", TaskStatus.TO_DO);
            assertValidationError(result, "description", "description must have at least 5 characters");
        }
    }

    private void assertValidationError(Result<Task, Error> result,
                                       String expectedField, String expectedMessage) {
        assertTrue(result.isError());
        assertEquals(expectedField, result.getError().field());
        assertEquals(expectedMessage, result.getError().message());
    }
}
