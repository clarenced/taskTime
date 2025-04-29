package io.github.clarenced.tasktime.tasks;

public class ErrorFactory {

    public static TaskTimeApi.ErrorDto taskNotFound(Long taskId) {
        return new TaskTimeApi.ErrorDto("taskId", "Task with id " + taskId + " does not exist");
    }

    static TaskTimeApi.ErrorDto titleHasMoreThan30Characters() {
        return new TaskTimeApi.ErrorDto("title", "title has more than 30 characters");
    }

    static TaskTimeApi.ErrorDto titleHasLessThan5Characters() {
        return new TaskTimeApi.ErrorDto("title", "title must have at least 5 characters");
    }

    static TaskTimeApi.ErrorDto descriptionHasLessThan5Characters() {
        return new TaskTimeApi.ErrorDto("description", "description must have at least 5 characters");
    }

    static TaskTimeApi.ErrorDto descriptionHasMore300Characters() {
        return new TaskTimeApi.ErrorDto("description", "description has more than 300 characters");
    }

    static TaskTimeApi.ErrorDto descriptionIsEmpty() {
        return new TaskTimeApi.ErrorDto("description", "description is empty");
    }

    static TaskTimeApi.ErrorDto titleIsEmpty() {
        return new TaskTimeApi.ErrorDto("title", "title is empty");
    }
}