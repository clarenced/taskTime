FROM eclipse-temurin:25-jre-alpine
LABEL org.opencontainers.image.source=https://github.com/clarenced/taskTime
LABEL org.opencontainers.image.description="TaskTime"
LABEL org.opencontainers.image.licenses=MIT

RUN addgroup -S spring && adduser -S -G spring spring
USER spring:spring

WORKDIR /app
COPY build/libs/taskTime-*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
