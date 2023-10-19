

#
# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-alpine
COPY --from=build /target/g4-backend-0.0.1-SNAPSHOT.jar backend.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","backend.jar"]