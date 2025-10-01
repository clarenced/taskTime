package io.github.clarenced.tasktime;

import io.github.clarenced.tasktime.api.PostgreSqlTestConfiguration;
import org.springframework.boot.SpringApplication;

public class TaskTimeTest {

    public static void main(String[] args) {
        SpringApplication
                .from(TaskTime::main)
                .with(PostgreSqlTestConfiguration.class)
                .run(args);
    }
}
