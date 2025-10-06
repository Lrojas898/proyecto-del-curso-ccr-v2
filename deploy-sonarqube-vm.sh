#!/bin/bash

# Script para desplegar SonarQube en Azure VM
# Autor: CCR Hospital Management Team
# Descripción: Crea una VM en Azure e instala SonarQube con Docker

set -e

# Variables de configuración
RESOURCE_GROUP="rg-sonarqube-ccr"
VM_NAME="vm-sonarqube-ccr"
LOCATION="chilecentral"  # Región de Chile
VM_SIZE="Standard_B1ms"  # 1 vCPU, 2GB RAM - mínimo para SonarQube (más económico)
ADMIN_USERNAME="azureuser"
NSG_NAME="nsg-sonarqube-ccr"

echo "🚀 Iniciando despliegue de SonarQube en Azure VM..."

# 1. Crear grupo de recursos
echo "📦 Creando grupo de recursos: $RESOURCE_GROUP"
az group create \
    --name $RESOURCE_GROUP \
    --location $LOCATION

# 2. Crear reglas de seguridad para SonarQube
echo "🔐 Creando grupo de seguridad de red..."
az network nsg create \
    --resource-group $RESOURCE_GROUP \
    --name $NSG_NAME

# Permitir SSH
az network nsg rule create \
    --resource-group $RESOURCE_GROUP \
    --nsg-name $NSG_NAME \
    --name AllowSSH \
    --protocol tcp \
    --priority 1000 \
    --destination-port-range 22 \
    --access allow

# Permitir SonarQube (puerto 9000)
az network nsg rule create \
    --resource-group $RESOURCE_GROUP \
    --nsg-name $NSG_NAME \
    --name AllowSonarQube \
    --protocol tcp \
    --priority 1001 \
    --destination-port-range 9000 \
    --access allow

# 3. Crear VM
echo "💻 Creando máquina virtual: $VM_NAME"
az vm create \
    --resource-group $RESOURCE_GROUP \
    --name $VM_NAME \
    --image Ubuntu2204 \
    --size $VM_SIZE \
    --admin-username $ADMIN_USERNAME \
    --generate-ssh-keys \
    --nsg $NSG_NAME \
    --public-ip-sku Standard

# 4. Obtener IP pública
VM_PUBLIC_IP=$(az vm show \
    --resource-group $RESOURCE_GROUP \
    --name $VM_NAME \
    --show-details \
    --query publicIps \
    --output tsv)

echo "🌐 IP pública de la VM: $VM_PUBLIC_IP"

# 5. Instalar Docker y SonarQube en la VM
echo "🐳 Instalando Docker y SonarQube..."
az vm run-command invoke \
    --resource-group $RESOURCE_GROUP \
    --name $VM_NAME \
    --command-id RunShellScript \
    --scripts '
    # Actualizar sistema
    sudo apt-get update

    # Instalar Docker
    sudo apt-get install -y docker.io docker-compose
    sudo systemctl start docker
    sudo systemctl enable docker
    sudo usermod -aG docker $USER

    # Configurar parámetros del sistema para SonarQube
    echo "vm.max_map_count=524288" | sudo tee -a /etc/sysctl.conf
    echo "fs.file-max=131072" | sudo tee -a /etc/sysctl.conf
    sudo sysctl -p

    # Crear directorio para SonarQube
    sudo mkdir -p /opt/sonarqube
    cd /opt/sonarqube

    # Crear docker-compose.yml para SonarQube optimizado para 2GB RAM
    sudo tee docker-compose.yml > /dev/null <<EOF
version: "3.8"

services:
  sonarqube:
    image: sonarqube:10.3-community
    container_name: sonarqube
    restart: unless-stopped
    environment:
      - SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true
      # Optimización para VM con 2GB RAM
      - SONAR_CE_JAVAOPTS=-Xmx512m -Xms128m
      - SONAR_WEB_JAVAOPTS=-Xmx512m -Xms128m
      - ES_JAVA_OPTS=-Xmx512m -Xms512m
    ports:
      - "9000:9000"
    volumes:
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_logs:/opt/sonarqube/logs
      - sonarqube_extensions:/opt/sonarqube/extensions
    mem_limit: 1.5g  # Limitar memoria a 1.5GB para dejar espacio al sistema
    ulimits:
      memlock:
        soft: -1
        hard: -1
      nofile:
        soft: 65536
        hard: 65536

volumes:
  sonarqube_data:
  sonarqube_logs:
  sonarqube_extensions:
EOF

    # Iniciar SonarQube
    sudo docker-compose up -d

    # Esperar a que SonarQube esté listo
    echo "⏳ Esperando a que SonarQube esté listo..."
    sleep 60

    # Verificar estado
    sudo docker-compose ps
    '

echo "✅ ¡Despliegue completado!"
echo ""
echo "📋 Información del despliegue:"
echo "   - Grupo de recursos: $RESOURCE_GROUP"
echo "   - VM: $VM_NAME"
echo "   - IP pública: $VM_PUBLIC_IP"
echo "   - SonarQube URL: http://$VM_PUBLIC_IP:9000"
echo "   - Usuario inicial: admin"
echo "   - Contraseña inicial: admin"
echo ""
echo "🔗 Para conectarte por SSH:"
echo "   ssh $ADMIN_USERNAME@$VM_PUBLIC_IP"
echo ""
echo "📝 Próximos pasos:"
echo "1. Acceder a SonarQube en http://$VM_PUBLIC_IP:9000"
echo "2. Cambiar la contraseña por defecto"
echo "3. Generar un token para el pipeline"
echo "4. Configurar el Service Connection en Azure DevOps"

# Guardar información en archivo
cat > deployment-info.txt <<EOF
SonarQube Deployment Information
================================
Resource Group: $RESOURCE_GROUP
VM Name: $VM_NAME
Public IP: $VM_PUBLIC_IP
SonarQube URL: http://$VM_PUBLIC_IP:9000
SSH Command: ssh $ADMIN_USERNAME@$VM_PUBLIC_IP

Default Credentials:
Username: admin
Password: admin

IMPORTANT: Change the default password immediately after first login!
EOF

echo "💾 Información guardada en deployment-info.txt"