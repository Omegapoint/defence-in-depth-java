# Build Java application
FROM maven:3-openjdk-17-slim AS build-env

# Setup build space
WORKDIR /app
COPY . ./

# Build jar
RUN mvn clean install

# Build runtime image
FROM openjdk:17-alpine

# Copy build
WORKDIR /app
ARG JAR_FILE=/app/target/*.jar
COPY --from=build-env ${JAR_FILE} app.jar

# Do not run as root user
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser
RUN chown -R appuser:appuser /app
USER appuser
RUN ls -la /app

# Launch application
ENTRYPOINT ["java","-jar","/app/app.jar"]

