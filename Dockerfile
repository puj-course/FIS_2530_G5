FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/greenet-1.0-SNAPSHOT.jar app.jar
RUN mkdir -p /data
EXPOSE 8080
ENV DB_URL=jdbc:h2:file:/data/greenet;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE
ENV DB_USERNAME=sa
ENV DB_PASSWORD=
CMD ["java", "-jar", "app.jar"]
