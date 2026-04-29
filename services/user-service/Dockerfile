# Build context: astra-commerce/ (repo root)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml ./pom.xml
COPY discovery-server/pom.xml ./discovery-server/pom.xml
COPY config-server/pom.xml ./config-server/pom.xml
COPY gateway/pom.xml ./gateway/pom.xml
COPY services/user-service/pom.xml ./services/user-service/pom.xml
COPY services/product-service/pom.xml ./services/product-service/pom.xml
COPY services/order-service/pom.xml ./services/order-service/pom.xml
COPY services/inventory-service/pom.xml ./services/inventory-service/pom.xml
COPY services/payment-service/pom.xml ./services/payment-service/pom.xml
COPY services/notification-service/pom.xml ./services/notification-service/pom.xml
RUN mvn install -N -DskipTests --no-transfer-progress
COPY services/user-service/ ./services/user-service/
RUN mvn package spring-boot:repackage -pl services/user-service -DskipTests --no-transfer-progress

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN addgroup --system astra && adduser --system --group astra && mkdir logs && chown astra:astra logs
USER astra
COPY --from=build /build/services/user-service/target/user-service-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
EXPOSE 8081