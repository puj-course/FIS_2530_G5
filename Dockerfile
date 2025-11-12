FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    openjdk-17-jdk \
    libgl1 \
    libglu1 \
    libglib2.0-0 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    && wget https://download2.gluonhq.com/openjfx/17.0.8/openjfx-17.0.8_linux-x64_bin-sdk.zip \
    && unzip openjfx-17.0.8_linux-x64_bin-sdk.zip -d /opt \
    && rm openjfx-17.0.8_linux-x64_bin-sdk.zip \
    && apt-get clean

WORKDIR /app
COPY target/greenet-1.0-SNAPSHOT.jar app.jar

ENV JAVA_FX_HOME=/opt/javafx-sdk-17.0.8

CMD echo "INICIANDO APLICACIÓN JAVAFX" && \
    echo "Conectando a H2 (en contenedor separado)" && \
    echo "URL: jdbc:h2:tcp://h2-database:1521/~/greenet" && \
    java --module-path=$JAVA_FX_HOME/lib --add-modules=javafx.controls,javafx.fxml -jar app.jar && \
    echo "DESPLIEGUE EXITOSO - JavaFX + H2 en contenedores separados"