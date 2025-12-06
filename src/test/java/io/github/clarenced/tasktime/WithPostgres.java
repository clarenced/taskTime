package io.github.clarenced.tasktime;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.github.clarenced.tasktime.api.PostgresSqlTestConfig;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * We draw inspiration from Spring Boot to define a Test slice that launches our Postgres containers.
 */
@Target( { TYPE })
@Retention( RUNTIME)
@Documented
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PostgresSqlTestConfig.class)
public @interface WithPostgres {
}
