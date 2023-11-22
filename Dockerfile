FROM eclipse-temurin:17-jdk-alpine AS build
LABEL "maintainer"="Henrique"
WORKDIR cashew

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean package -DskipTests

# Extrair os arquivos
FROM eclipse-temurin:17-jdk-alpine

ENV AWS_REGION=sa-east-1
ENV AWS_DEFAULT_OUTPUT=json
ENV AWS_ACCESS_KEY_ID=123
ENV AWS_SECRET_ACCESS_KEY=123
ENV AWS_ENDPOINT_URL=http://localstack:4566

WORKDIR cashew

ARG JAR_LOCATION=cashew/target

COPY --from=build ${JAR_LOCATION}/cashew.jar .

EXPOSE 8080
EXPOSE 5005

ENTRYPOINT ["java", "-jar", "cashew.jar"]
