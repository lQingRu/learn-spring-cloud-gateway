FROM gradle:latest as builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build

FROM adoptopenjdk/openjdk14:ubi

RUN mkdir -p /spring-gateway/src

ARG JAR_FILE=/home/gradle/src/build/libs/spring-gateway-0.0.1-SNAPSHOT.jar

WORKDIR mkdir /spring-gateway/src

COPY --from=builder ${JAR_FILE} spring-gateway-v1.jar

ADD src/main/resources/application.yml //

EXPOSE 8100

# Tell docker to run the application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "spring-gateway-v1.jar"]

