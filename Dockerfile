FROM java:8-jdk-alpine
RUN pwd
COPY build/libs/*.jar cloudInAction-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "cloudInAction-0.0.1-SNAPSHOT.jar"]