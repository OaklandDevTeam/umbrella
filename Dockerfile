FROM gradle:4.7.0-jdk8-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# RUN echo "http://dl-cdn.alpinelinux.org/alpine/latest-stable/main" > /etc/apk/repositories
# RUN echo "http://dl-cdn.alpinelinux.org/alpine/latest-stable/community" >> /etc/apk/repositories
# RUN apk --no-cache --update-cache add gcc gfortran python python-dev py-pip build-base wget freetype-dev libpng-dev openblas-dev
RUN gradle build --no-daemon 

FROM java:8-jdk-alpine
WORKDIR /usr/app
COPY --from=build /home/gradle/src/build/libs/*.jar /usr/app/umbrella.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "umbrella.jar"]