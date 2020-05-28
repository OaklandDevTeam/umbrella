FROM gradle:6.4.1-jdk8 AS build
USER root
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM java:8-jdk-alpine
WORKDIR /usr/app
COPY --from=build /home/gradle/src/build/libs/*.jar /usr/app/umbrella.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "umbrella.jar"]