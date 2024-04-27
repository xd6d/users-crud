FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml -DskipTests clean install

FROM gcr.io/distroless/java17-debian12
COPY --from=build /usr/src/app/target/users-crud-0.0.1-SNAPSHOT.jar /usr/app/users-crud-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/users-crud-0.0.1-SNAPSHOT.jar"]