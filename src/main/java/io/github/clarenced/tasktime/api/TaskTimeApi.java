package io.github.clarenced.tasktime.api;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskTimeApi {

   public record TaskDto(String title, String description){}

    @GetMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaskDto>> getAllTasks(){
        var tasks = List.of(
                new TaskDto("Prepare slides for the Spring meetup", "Prepare slides for the Spring meetup"),
                new TaskDto("Go to the theater", "Go to the theater"));
        return ResponseEntity.ok(tasks);
    }
}
