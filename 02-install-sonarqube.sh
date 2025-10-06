#!/bin/bash
# Script 2: Instalar SonarQube en la VM
# Ejecutar: ./02-install-sonarqube.sh

set -e

RESOURCE_GROUP="rg-sonarqube-ccr-v2"
VM_NAME="vm-sonarqube-ccr"

echo "🚀 Paso 2: Instalando SonarQube..."

# Obtener IP de la VM
echo "📋 Obteniendo IP de la VM..."
VM_IP=$(az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME --show-details --query publicIps --output tsv)
echo "🌐 IP de la VM: $VM_IP"

# Verificar que la VM esté corriendo
echo "🔍 Verificando estado de la VM..."
VM_STATE=$(az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME --show-details --query powerState --output tsv)
echo "⚡ Estado: $VM_STATE"

if [ "$VM_STATE" != "VM running" ]; then
    echo "❌ Error: La VM no está corriendo. Estado: $VM_STATE"
    exit 1
fi

echo "✅ VM está corriendo correctamente"

# Crear script de instalación remoto
cat > /tmp/install-sonarqube.sh << 'EOF'
#!/bin/bash
set -e

echo "🔄 Actualizando sistema..."
sudo apt-get update -y

echo "🐳 Instalando Docker..."
sudo apt-get install -y docker.io docker-compose
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker $USER

echo "⚙️ Configurando parámetros del sistema para SonarQube..."
echo 'vm.max_map_count=524288' | sudo tee -a /etc/sysctl.conf
echo 'fs.file-max=131072' | sudo tee -a /etc/sysctl.conf
sudo sysctl -p

echo "📁 Creando directorio para SonarQube..."
sudo mkdir -p /opt/sonarqube
sudo chown $(whoami):$(whoami) /opt/sonarqube
cd /opt/sonarqube

echo "📝 Creando docker-compose.yml..."
cat > docker-compose.yml << 'DOCKER_EOF'
version: '3.8'

services:
  sonarqube:
    image: sonarqube:10.3-community
    container_name: sonarqube
    restart: unless-stopped
    environment:
      - SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true
      - SONAR_CE_JAVAOPTS=-Xmx1g -Xms256m
      - SONAR_WEB_JAVAOPTS=-Xmx1g -Xms256m
      - ES_JAVA_OPTS=-Xmx1g -Xms1g
    ports:
      - "9000:9000"
    volumes:
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_logs:/opt/sonarqube/logs
      - sonarqube_extensions:/opt/sonarqube/extensions
    mem_limit: 3g
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
DOCKER_EOF

echo "🚀 Iniciando SonarQube..."
docker-compose up -d

echo "⏳ Esperando 30 segundos para que SonarQube inicie..."
sleep 30

echo "📊 Verificando estado del contenedor..."
docker-compose ps

echo "✅ SonarQube instalado y en ejecución!"
echo "🌐 Accede a SonarQube en: http://$(curl -s ifconfig.me):9000"
echo "👤 Usuario: admin"
echo "🔑 Contraseña: admin"
EOF

echo "📤 Copiando script a la VM..."
scp -o StrictHostKeyChecking=no -o ConnectTimeout=30 /tmp/install-sonarqube.sh azureuser@$VM_IP:/tmp/

echo "🔧 Ejecutando instalación en la VM..."
ssh -o StrictHostKeyChecking=no -o ConnectTimeout=30 azureuser@$VM_IP "chmod +x /tmp/install-sonarqube.sh && /tmp/install-sonarqube.sh"

echo ""
echo "🎉 SonarQube instalado exitosamente!"
echo "🌐 URL: http://$VM_IP:9000"
echo "👤 Usuario inicial: admin"
echo "🔑 Contraseña inicial: admin"
echo ""
echo "⏭️  Siguiente paso: Ejecutar ./03-verify-sonarqube.sh"

# Guardar información
cat > deployment-info.txt << EOF
SonarQube Deployment Information
================================
VM IP: $VM_IP
SonarQube URL: http://$VM_IP:9000
SSH Command: ssh azureuser@$VM_IP

Initial Credentials:
Username: admin
Password: admin

Resource Group: $RESOURCE_GROUP
VM Name: $VM_NAME
Location: Chile Central
VM Size: Standard_B2s (2 vCPUs, 4GB RAM)

Date Created: $(date)
EOF

echo "📄 Información guardada en deployment-info.txt"