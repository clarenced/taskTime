package io.github.clarenced.tasktime.tasks.api;

public class ErrorFactory {

    public static TaskTimeApi.ErrorDto taskNotFound(Long taskId) {
        return new TaskTimeApi.ErrorDto("taskId", "Task with id " + taskId + " does not exist");
    }

    public static TaskTimeApi.ErrorDto titleHasMoreThan30Characters() {
        return new TaskTimeApi.ErrorDto("title", "title has more than 30 characters");
    }

    public static TaskTimeApi.ErrorDto titleHasLessThan5Characters() {
        return new TaskTimeApi.ErrorDto("title", "title must have at least 5 characters");
    }

    public static TaskTimeApi.ErrorDto descriptionHasLessThan5Characters() {
        return new TaskTimeApi.ErrorDto("description", "description must have at least 5 characters");
    }

    public static TaskTimeApi.ErrorDto descriptionHasMore300Characters() {
        return new TaskTimeApi.ErrorDto("description", "description has more than 300 characters");
    }

    public static TaskTimeApi.ErrorDto descriptionIsEmpty() {
        return new TaskTimeApi.ErrorDto("description", "description is empty");
    }

    public static TaskTimeApi.ErrorDto titleIsEmpty() {
        return new TaskTimeApi.ErrorDto("title", "title is empty");
    }
}