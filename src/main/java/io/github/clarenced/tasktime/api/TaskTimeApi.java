package io.github.clarenced.tasktime.api;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskTimeApi {

   public record TaskDto(Long id, String title, String description){}

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAllTasks(){
        var tasks = getTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping(value = "/tasks/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> getTaskById(@PathVariable int taskId){
       return getTasks().stream()
               .filter(task -> task.id() == taskId)
               .findFirst()
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private static List<TaskDto> getTasks() {
        return List.of(
                new TaskDto(1L,"Prepare slides for the Spring meetup", "Prepare slides for the Spring meetup"),
                new TaskDto(3L, "Go to the theater", "Go to the theater"));
    }
}
