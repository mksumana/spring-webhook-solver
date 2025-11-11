# Spring Webhook Solver
Spring Boot app that runs automatically on startup to:
1. Generate webhook + token.
2. Select question based on regNo.
3. Produce SQL (placeholder).
4. Send SQL to returned webhook using JWT header.
## Build & Run
mvn clean package
java -jar target/spring-webhook-solver-0.0.1-SNAPSHOT.jar
## To Submit
Upload public GitHub repo + raw JAR link.