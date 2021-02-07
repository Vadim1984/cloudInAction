FROM gradle:latest as build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
# have to set root user, because /home/gradle/src has root permisions and gradle cannot create build dir during compilation
USER root
RUN gradle build --no-daemon

FROM java:8-jdk-alpine
WORKDIR /usr/app
COPY --from=build /home/gradle/src/build/libs/*.jar ./cloudInAction-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "cloudInAction-0.0.1-SNAPSHOT.jar"]