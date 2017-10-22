FROM maven:alpine AS build

WORKDIR /usr/src/app

COPY src src
COPY lombok.config lombok.config
COPY pom.xml pom.xml

RUN mvn clean package

FROM openjdk:jre-alpine

EXPOSE 8080

COPY --from=build /usr/src/app/target/flowbot.jar /usr/src/app/target/flowbot.jar

CMD ["java", "-Xmx500m", "-jar", "/usr/src/app/target/flowbot.jar", "server", "server.yml"]