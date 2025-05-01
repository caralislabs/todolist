# STAGE 1: Build the JAR
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean bootJar

# STAGE 2: Run the JAR
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy only the built JAR from the previous stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
