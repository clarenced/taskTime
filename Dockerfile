FROM oraclelinux:9-slim
LABEL org.opencontainers.image.source=https://github.com/clarenced/taskTime
LABEL org.opencontainers.image.description="TaskTime"
LABEL org.opencontainers.image.licenses=MIT

WORKDIR /app
COPY build/native/nativeCompile/taskTime ./application
EXPOSE 8080
ENTRYPOINT ["./application"]
