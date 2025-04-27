package io.github.clarenced.tasktime.api;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TaskTimeApi {

    private final TaskRepository taskRepository = new TaskRepository();

    public enum TaskStatus {TO_DO, IN_PROGRESS, DONE}
    public record TaskDto(Long id, String title, String description, TaskStatus status){
        public TaskDto(Long id, String title, String description) {
            this(id, title, description, TaskStatus.TO_DO);
        }
    }
    public record CreateTaskDto(String title, String description){}
    public record ErrorDto(String field, String message){}

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAllTasks(){
        return ResponseEntity.ok(this.taskRepository.getTasks());
    }

    @GetMapping(value = "/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTaskById(@PathVariable int taskId){
       return this.taskRepository.getTasks().stream()
               .filter(task -> task.id() == taskId)
               .findFirst()
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDto createTaskDto){
        return validateTask(createTaskDto)
                .map(errorDto -> ResponseEntity.badRequest().body(errorDto))
                .orElseGet(() -> {
                    taskRepository.createTask(createTaskDto);
                    return ResponseEntity.status(201).build();
                });
    }

    private Optional<ErrorDto> validateTask(CreateTaskDto createTaskDto){
        if(createTaskDto.title().isEmpty()){
            return Optional.of(new ErrorDto("title", "title is empty"));
        }
        if(createTaskDto.description().isEmpty()){
            return Optional.of(new ErrorDto("description", "description is empty"));
        }
        return Optional.empty();
    }
}
