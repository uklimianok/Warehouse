FROM maven:4.0.0-rc-5-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml /build/pom.xml
RUN mvn dependency:go-offline
COPY src /build/src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/target/warehouse-app-2.1.0.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]