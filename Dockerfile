

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
COPY --from=build /target/MPodcasts-backend-0.0.1-SNAPSHOT.jar MPodcasts.jar
ENTRYPOINT ["java","-jar","MPodcasts.jar"]