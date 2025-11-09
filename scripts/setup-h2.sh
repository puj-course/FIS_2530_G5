#!/bin/bash

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}"
echo " Iniciando instalación de H2 Database"
echo "=========================================="
echo -e "${NC}"

# Verificar Docker
if ! command -v docker &> /dev/null; then
    echo -e "${RED} Docker no está instalado${NC}"
    echo "Por favor instala Docker primero:"
    echo "  https://docs.docker.com/get-docker/"
    exit 1
fi

echo -e "${GREEN} Docker encontrado${NC}"

# Verificar Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo -e "${YELLOW}  Docker Compose no encontrado, instalando...${NC}"
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
fi

echo -e "${GREEN} Docker Compose listo${NC}"

# Iniciar H2
echo -e "\n Descargando e iniciando H2 Database..."
docker-compose -f docker-compose.h2.yml up -d

# Esperar a que H2 esté listo
echo -e "\n Esperando a que H2 inicie..."
sleep 10

# Verificar estado
if docker-compose -f docker-compose.h2.yml ps | grep -q "Up"; then
    echo -e "${GREEN} H2 Database está corriendo${NC}"
else
    echo -e "${RED} Error al iniciar H2${NC}"
    docker-compose -f docker-compose.h2.yml logs
    exit 1
fi

# Mostrar información de conexión
echo -e "\n${GREEN}"
echo "=========================================="
echo " H2 DATABASE INSTALADO CORRECTAMENTE"
echo "=========================================="
echo -e "${NC}"
echo " Consola Web: http://localhost:8082"
echo " JDBC URL:    jdbc:h2:tcp://localhost:1521/~/mydb"
echo " Usuario:     sa"
echo " Password:    (vacío)"
echo ""
echo " Para ver logs: docker-compose -f docker-compose.h2.yml logs"
echo " Para detener: docker-compose -f docker-compose.h2.yml down"
echo -e "${GREEN}"
echo "=========================================="
echo -e "${NC}"
