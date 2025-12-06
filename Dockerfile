FROM ghcr.io/graalvm/graalvm-community:25.0.1-ol8-20251021
WORKDIR /app
COPY build/native/nativeCompile/taskTime ./application
EXPOSE 8080
ENTRYPOINT ["./application"]
