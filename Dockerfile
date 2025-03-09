FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

RUN apt-get update && apt-get install -y maven

COPY pom.xml .

COPY src/ ./src/

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
