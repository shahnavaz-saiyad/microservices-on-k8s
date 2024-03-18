FROM openjdk:17-alpine

RUN mkdir /app

COPY target/app-config.jar /app/app-config.jar

CMD ["java", "-jar", "./app/app-config.jar"]