FROM openjdk:20-jdk-slim-bullseye
ADD target/pub-sub-server.jar pub-sub-server.jar
ENTRYPOINT ["java", "-jar","pub-sub-server.jar"]
EXPOSE 8080