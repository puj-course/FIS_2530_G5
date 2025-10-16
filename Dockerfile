FROM eclipse-temurin:24-jre
COPY src/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
