FROM openjdk:17

WORKDIR /app

COPY ./target/*.jar ./spring-boot-HowTo-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT java -jar spring-boot-HowTo-0.0.1-SNAPSHOT.jar
