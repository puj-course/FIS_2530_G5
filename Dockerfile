FROM ubuntu:20.04
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    openjdk-17-jdk \
    && wget https://download2.gluonhq.com/openjfx/17.0.8/openjfx-17.0.8_linux-x64_bin-sdk.zip \
    && unzip openjfx-17.0.8_linux-x64_bin-sdk.zip -d /opt \
    && rm openjfx-17.0.8_linux-x64_bin-sdk.zip

WORKDIR /app
COPY target/greenet-1.0-SNAPSHOT.jar app.jar
RUN mkdir -p /data
EXPOSE 8080
ENV DB_URL=jdbc:h2:file:/data/greenet;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE
ENV DB_USERNAME=sa
ENV DB_PASSWORD=

CMD ["java", "--module-path", "/opt/javafx-sdk-17.0.8/lib", "--add-modules", "javafx.controls,javafx.fxml,javafx.base", "-jar", "app.jar"]
