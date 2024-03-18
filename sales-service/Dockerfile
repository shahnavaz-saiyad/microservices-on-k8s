FROM openjdk:17-alpine

RUN mkdir /app

COPY target/sales-service.jar /app/sales-service.jar

CMD ["java", "-jar", "./app/sales-service.jar"]