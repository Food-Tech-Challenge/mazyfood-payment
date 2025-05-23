FROM maven:3.9.9-amazoncorretto-21 AS builder

WORKDIR /app

COPY pom.xml pom.xml
RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]