FROM java:8-jdk-alpine
WORKDIR /usr/app
COPY ./build/libs/umbrella-1.0-SNAPSHOT.jar /usr/app
EXPOSE 80
ENTRYPOINT ["java", "-jar", "umbrella-1.0-SNAPSHOT.jar"]