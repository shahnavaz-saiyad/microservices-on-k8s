FROM openjdk:17-alpine

RUN mkdir /app

COPY target/user-service.jar /app/user-service.jar

CMD ["java", "-jar", "./app/user-service.jar"]