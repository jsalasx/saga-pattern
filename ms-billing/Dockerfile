FROM eclipse-temurin:21-jre
WORKDIR /app
COPY ./ms-billing/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
