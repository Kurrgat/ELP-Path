# Use Maven to build the Java application
FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /ELP_PATH_BE
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use OpenJDK to run the application
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /ELP_PATH_BE/mvn/wrapper/maven-wrapper.jar.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
