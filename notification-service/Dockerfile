FROM openjdk:17-jdk-slim-buster
WORKDIR /app
ARG JAR_FILE
COPY ./target/${JAR_FILE} app.jar
EXPOSE 8084
CMD ["java", "-jar", "app.jar"]