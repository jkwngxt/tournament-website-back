FROM alpine/java:21-jdk
EXPOSE 8080
COPY target/tournament-website-back.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--server.port=${PORT}"]

