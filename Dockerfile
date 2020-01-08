FROM openjdk:8-jdk-alpine

MAINTAINER  Juergen Reiss <juergen.a.reiss@web.de>

RUN addgroup -S javarunner && adduser -S javarunner -G javarunner
USER javarunner:javarunner

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

