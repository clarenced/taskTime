package io.github.clarenced.tasktime.api;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskTimeApi {

    private final TaskService taskService;

    public TaskTimeApi(TaskService taskService) {
        this.taskService = taskService;
    }

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
        return ResponseEntity.ok(this.taskService.getTasks());
    }

    @GetMapping(value = "/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTaskById(@PathVariable int taskId){
        return taskService.findTaskById(taskId)
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDto createTaskDto){
        Result<Void, ErrorDto> result = taskService.createTask(createTaskDto);
        if(result.isError()){
            return ResponseEntity.badRequest().body(result.getError());
        }
        return result.map(_ -> ResponseEntity.status(201).build()).getSuccess();
    }
}
