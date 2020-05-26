FROM gradle:5.5.1-jdk8 AS build
USER root
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
#  RUN   apk update \                                                                                                                                                                                                                        
#   &&   apk add ca-certificates wget \                                                                                                                                                                                                      
# &&   update-ca-certificates 
# RUN wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub
# RUN wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.31-r0/glibc-2.31-r0.apk
# RUN apk add glibc-2.31-r0.apk

# RUN gradle npmInstall --no-daemon --stacktrace
RUN gradle build --no-daemon --stacktrace

FROM java:8-jdk-alpine
WORKDIR /usr/app
COPY --from=build /home/gradle/src/build/libs/*.jar /usr/app/umbrella.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "umbrella.jar"]