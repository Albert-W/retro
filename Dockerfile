FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT java -Dspring.profiles.active=production -Dserver.port=8090 -jar /app.jar