# Dockerfile

# jdk17 Image Start
FROM openjdk:17

ARG JAR_FILE=build/libs/zerocommission-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} zc_Backend.jar
ENTRYPOINT ["java","-jar","zc_Backend.jar"]
