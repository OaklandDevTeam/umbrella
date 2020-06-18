# Umbrella

![Umbrella Logo](frontend/src/assets/umbrella-logo.png)

- [Umbrella](#umbrella)
- [Dependencies (Gradle dependencies not included)](#dependencies-gradle-dependencies-not-included)
- [Building](#building)
    - [Building with Gradle on Windows](#building-with-gradle-on-windows)
    - [Building with Gradle on Mac/Linux](#building-with-gradle-on-maclinux)
- [Running](#running)
  - [Troubleshooting](#troubleshooting)
- [Building with Docker](#building-with-docker)

Umbrella is a web application that serves as a platform for discussion. Users, once created, can login, logout, join specific discussion groups (drops) and post. These drops are a space for discussion of a particular topic. Once posted under a drop, the users will have the ability to comment on a post as well. A post can be made up of text, pictures, and links.

# Dependencies (Gradle dependencies not included)
```
OpenJDK 1.8
Mongo Atlas
Docker (OPTIONAL: if performing docker build)
```
[AdoptOpenJDK](https://adoptopenjdk.net/) is a great choice.

[Mongo Atlas](https://www.mongodb.com/cloud/atlas)

# Building

Clone the repository and cd to project root:
```
git clone https://github.com/OaklandDevTeam/umbrella.git
cd umbrella 
```

To run the application, you need credentials to a Mongo Atlas instance. These credentials live in `dbconnection.properties` and must be supplied at build time. 
The project is supplied with a database.properties.TEMPLATE file. Copy and rename to `dbconnection.properties`. Its contents look like the following:

```
mongo.user=<MONGOUSER>
mongo.password=<MONGOPASSWORD>
mongo.host=<MONGOHOST>
```

This file needs to be populated with the database credentials and a Mongo Atlas host

### Building with Gradle on Windows
Run Gradle wrapper
```
./gradlew.bat build
```

### Building with Gradle on Mac/Linux
```
./gradlew build
```

A fresh build can take a while (roughly 5 minutes), so feel free to grab a cup of coffee. Docker builds take up to 9 minutes

# Running
After the project successfully builds, a jar file is produced:
```
./build/libs/umbrella-<version>.jar
```

You can run this JAR file by issuing the following command
```
java -jar umbrella-<version>.jar
```

## Troubleshooting
* By default, the application binds to port 80. If you are running on a user that is unpriveleged, the application may fail to bind. Supplying the following envrionment variable will bind the application to port `8080`

```
UMBRELLA_DEBUG=true java -jar umbrella-<version>.jar
```

* If you did not supply a dbconnection.properties file with correct credentials at build time, the application will fail to start. Supplying the following environment variables will allow the application to connect

```
MONGO_HOST=<MONGO ATLAS HOST> MONGO_USER=<MONGO USER> MONGO_PASS=<MONGO PASSWORD> java -jar umbrella-<version>.jar
```


# Building with Docker
Instead of building with Gradle, Docker can handle the build for you.

Execute
```
docker build
```

and then run using `docker run`