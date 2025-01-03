# Use Maven to build the Java application
FROM maven:3.9.4-openjdk-17-slim AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use OpenJDK to run the application
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/your-backend-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
