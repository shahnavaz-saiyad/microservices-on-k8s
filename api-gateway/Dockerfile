FROM openjdk:17-alpine

RUN mkdir /app

COPY target/api-gateway.jar /app/api-gateway.jar

CMD ["java", "-jar", "./app/api-gateway.jar"]