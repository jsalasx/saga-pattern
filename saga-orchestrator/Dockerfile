FROM eclipse-temurin:21-jre
WORKDIR /app
COPY ./saga-orchestrator/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
