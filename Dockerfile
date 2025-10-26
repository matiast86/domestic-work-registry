# ============================
# 🧱 1️⃣ Build stage
# ============================
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set working directory inside container
WORKDIR /app

# Copy only the pom first for dependency caching
COPY pom.xml .

# Pre-download dependencies to leverage Docker layer caching
RUN mvn dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Build the application JAR (skip tests for faster builds)
RUN mvn clean package -DskipTests

# ============================
# 🚀 2️⃣ Runtime stage
# ============================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Set working directory
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the Spring Boot default port
EXPOSE 8080

# Optionally set active profile
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
