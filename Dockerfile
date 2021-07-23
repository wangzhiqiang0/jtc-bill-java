FROM gradle:6.7.0-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build

FROM openjdk:8-jre-slim
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/jwtapi.jar
ENTRYPOINT ["java","-Dspring.profiles.active=container", "-jar", "/app/jwtapi.jar"]