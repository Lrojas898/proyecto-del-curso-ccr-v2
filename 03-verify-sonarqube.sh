#!/bin/bash
# Script 3: Verificar que SonarQube esté funcionando
# Ejecutar: ./03-verify-sonarqube.sh

set -e

RESOURCE_GROUP="rg-sonarqube-ccr-v2"
VM_NAME="vm-sonarqube-ccr"

echo "🚀 Paso 3: Verificando SonarQube..."

# Obtener IP de la VM
VM_IP=$(az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME --show-details --query publicIps --output tsv)
echo "🌐 Verificando SonarQube en: http://$VM_IP:9000"

# Función para verificar si SonarQube responde
check_sonarqube() {
    local max_attempts=30
    local attempt=1

    echo "🔍 Verificando conectividad (máximo $max_attempts intentos)..."

    while [ $attempt -le $max_attempts ]; do
        echo "   Intento $attempt/$max_attempts..."

        if curl -s --connect-timeout 10 http://$VM_IP:9000/api/system/status > /dev/null 2>&1; then
            echo "✅ SonarQube está respondiendo!"
            return 0
        fi

        if [ $attempt -eq $max_attempts ]; then
            echo "❌ SonarQube no responde después de $max_attempts intentos"
            return 1
        fi

        echo "   ⏳ Esperando 10 segundos..."
        sleep 10
        ((attempt++))
    done
}

# Verificar estado de la VM
echo "📊 Estado de la VM:"
az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME --show-details --query "{name:name, powerState:powerState, publicIps:publicIps}" --output table

# Verificar conectividad básica
echo ""
echo "🔗 Probando conectividad SSH..."
if ssh -o StrictHostKeyChecking=no -o ConnectTimeout=10 azureuser@$VM_IP "echo 'SSH OK'" 2>/dev/null; then
    echo "✅ SSH conectividad OK"
else
    echo "❌ Error de conectividad SSH"
    exit 1
fi

# Verificar estado del contenedor
echo ""
echo "🐳 Verificando estado del contenedor..."
ssh -o StrictHostKeyChecking=no azureuser@$VM_IP "cd /opt/sonarqube && docker-compose ps"

# Verificar logs recientes de SonarQube
echo ""
echo "📋 Últimos logs de SonarQube:"
ssh -o StrictHostKeyChecking=no azureuser@$VM_IP "cd /opt/sonarqube && docker-compose logs sonarqube --tail 10"

# Verificar que SonarQube responda
echo ""
if check_sonarqube; then
    # Obtener información de estado
    echo ""
    echo "📊 Estado de SonarQube:"
    SONAR_STATUS=$(curl -s http://$VM_IP:9000/api/system/status 2>/dev/null || echo '{"status":"ERROR"}')
    echo "$SONAR_STATUS" | jq . 2>/dev/null || echo "$SONAR_STATUS"

    echo ""
    echo "🎉 ¡SonarQube está funcionando correctamente!"
    echo ""
    echo "🌐 Acceso web: http://$VM_IP:9000"
    echo "👤 Usuario: admin"
    echo "🔑 Contraseña: admin"
    echo ""
    echo "📋 Próximos pasos:"
    echo "   1. Abrir http://$VM_IP:9000 en tu navegador"
    echo "   2. Hacer login con admin/admin"
    echo "   3. Cambiar contraseña por defecto"
    echo "   4. Ejecutar ./04-configure-azure-devops.sh"

else
    echo ""
    echo "❌ SonarQube no está respondiendo correctamente"
    echo "🔧 Comandos para diagnosticar:"
    echo "   ssh azureuser@$VM_IP"
    echo "   cd /opt/sonarqube"
    echo "   docker-compose logs sonarqube"
    echo "   docker-compose restart sonarqube"
    exit 1
fi

# Actualizar archivo de información
cat >> deployment-info.txt << EOF

Verification Results ($(date)):
================================
SonarQube Status: WORKING
URL: http://$VM_IP:9000
SSH: ssh azureuser@$VM_IP

Next Steps:
1. Open browser: http://$VM_IP:9000
2. Login: admin/admin
3. Change default password
4. Run: ./04-configure-azure-devops.sh
EOF

echo ""
echo "📄 Información actualizada en deployment-info.txt"