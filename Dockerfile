FROM maven:3.9.1-amazoncorretto-17 AS builder
WORKDIR /api
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package --no-transfer-progress -DskipTests

FROM amazoncorretto:17-alpine3.14
WORKDIR /api
COPY --from=builder /api/target/api-gestion-clientes.jar ./api-gestion-clientes.jar
CMD ["sh", "-c", "sleep 300 && java -jar api-gestion-clientes.jar"]