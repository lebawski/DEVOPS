FROM openjdk:17
EXPOSE 8080
ADD target/Docker-integration.jar Docker-integration.jar

ENTRYPOINT ["java","-jar","/Docker-integration.jar"]