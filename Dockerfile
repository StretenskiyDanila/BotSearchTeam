FROM maven:3.9.4-eclipse-temurin-17 as build


COPY src src
COPY pom.xml pom.xml

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

RUN adduser --system bot-search-team && addgroup --system bot-search-team && adduser bot-search-team bot-search-team
USER bot-search-team

WORKDIR /app

COPY --from=build target/SearchTeamBot.jar ./SearchTeamBot.jar
COPY --from=build target/dependency ./lib

