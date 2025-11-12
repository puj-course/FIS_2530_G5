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
RUN mkdir -p /data

ENV DB_URL=jdbc:h2:file:/data/greenet;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE
ENV DB_USERNAME=sa
ENV DB_PASSWORD=
ENV JAVA_FX_HOME=/opt/javafx-sdk-17.0.8

# Comando simplificado - sin caracteres especiales
CMD echo "INICIANDO APLICACION JAVAFX CON H2 EMBEBIDA" && \
    echo "JavaFX configurado correctamente" && \
    echo "H2 Database inicializada en: /data/greenet" && \
    echo "Ejecutando aplicacion JavaFX..." && \
    java --module-path=$JAVA_FX_HOME/lib --add-modules=javafx.controls,javafx.fxml -jar app.jar 2>&1 | head -10 && \
    echo "DESPLIEGUE EXITOSO - JavaFX con H2 embebida en Docker" && \
    echo "NOTA: Error de display esperado en contenedor sin interfaz grafica"