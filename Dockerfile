# Build stage

FROM gradle:6.3.0-jdk11 AS build
COPY --chown=gradle:gradle . /home/app
WORKDIR /home/app
RUN gradle build --no-daemon

# Package stage

FROM openjdk:8-jre-slim

RUN mkdir /app
EXPOSE 8080
COPY --from=build /home/app/build/libs/*.jar /app/shop-api.jar

ENTRYPOINT ["java","-jar","/app/shop-api.jar"]


