# Dockerfile
FROM amazoncorretto:17-alpine
VOLUME /tmp
COPY target/*.jar crypto.jar
ENTRYPOINT ["java","-jar","/crypto.jar"]
ENV JAVA_OPTS="-Xmx128m -Xms64m"