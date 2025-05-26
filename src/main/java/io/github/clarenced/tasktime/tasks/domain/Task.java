package io.github.clarenced.tasktime.tasks.domain;

import io.github.clarenced.tasktime.common.Result;

import java.util.Objects;

public final class Task {
    private static final int TITLE_MAXIMUM_LENGTH = 100;
    private static final int TITLE_MINIMAL_LENGTH = 5;
    private static final int DESCRIPTION_MAXIMUM_LENGTH = 500;
    private static final int DESCRIPTION_MINIMUM_LENGTH = 5;

    private final Long id;
    private final String title;
    private final String description;
    private final TaskStatus status;


    private Task() {
        this(null, null, null, null);
    }

    private Task(Long id, String title, String description, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public static Result<Task, Error> create(String title, String description, TaskStatus status) {
        return create(null, title, description, status);
    }

    public static Result<Task, Error> create(Long id, String title, String description, TaskStatus status) {
        if(title == null || title.isBlank()) {
            return Result.error(new Error("title", "title is empty"));
        }
        if(title.length() > TITLE_MAXIMUM_LENGTH) {
            return Result.error(new Error("title", "title has more than 100 characters"));
        }
        if(title.length() < TITLE_MINIMAL_LENGTH) {
            return Result.error(new Error("title", "title must have at least 5 characters"));
        }
        if(description == null || description.isBlank()) {
            return Result.error(new Error("description", "description is empty"));
        }
        if(description.length() > DESCRIPTION_MAXIMUM_LENGTH) {
            return Result.error(new Error("description", "description has more than 500 characters"));
        }
        if(description.length() < DESCRIPTION_MINIMUM_LENGTH) {
            return Result.error(new Error("description", "description must have at least 5 characters"));
        }

        return Result.success(new Task(id, title, description, status));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Task) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.title, that.title) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status);
    }

    @Override
    public String toString() {
        return "Task[" +
                "id=" + id + ", " +
                "title=" + title + ", " +
                "description=" + description + ", " +
                "status=" + status + ']';
    }


}
