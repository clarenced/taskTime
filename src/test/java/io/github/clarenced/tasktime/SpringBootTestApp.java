package io.github.clarenced.tasktime;

import io.github.clarenced.tasktime.api.PostgresSqlTestConfig;
import org.springframework.boot.SpringApplication;

public class SpringBootTestApp {

    public static void main(String[] args) {
        SpringApplication
                .from(TaskTime::main)
                .with(PostgresSqlTestConfig.class)
                .run(args);
    }
}
