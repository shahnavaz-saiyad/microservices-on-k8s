FROM openjdk:17-alpine

RUN mkdir /app

COPY target/tenant-service.jar /app/tenant-service.jar

CMD ["java", "-jar", "./app/tenant-service.jar"]