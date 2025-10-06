#!/bin/bash
# Script 1: Crear VM para SonarQube
# Ejecutar: ./01-create-vm.sh

set -e

echo "🚀 Paso 1: Creando VM para SonarQube..."

# Variables
RESOURCE_GROUP="rg-sonarqube-ccr-v2"
VM_NAME="vm-sonarqube-ccr"
LOCATION="chilecentral"
VM_SIZE="Standard_B2s"  # 2 vCPUs, 4GB RAM - mejor para SonarQube
ADMIN_USERNAME="azureuser"

echo "📋 Configuración:"
echo "  - Resource Group: $RESOURCE_GROUP"
echo "  - VM Name: $VM_NAME"
echo "  - Location: $LOCATION"
echo "  - Size: $VM_SIZE (2 vCPUs, 4GB RAM)"
echo "  - Admin User: $ADMIN_USERNAME"
echo ""

# Crear resource group
echo "🔨 Creando resource group..."
az group create \
    --name $RESOURCE_GROUP \
    --location $LOCATION

echo "✅ Resource group creado"

# Crear VM
echo "🔨 Creando VM (esto toma 2-3 minutos)..."
az vm create \
    --resource-group $RESOURCE_GROUP \
    --name $VM_NAME \
    --image Ubuntu2204 \
    --size $VM_SIZE \
    --admin-username $ADMIN_USERNAME \
    --generate-ssh-keys \
    --public-ip-sku Standard \
    --public-ip-address-allocation static \
    --security-type TrustedLaunch \
    --enable-secure-boot true \
    --enable-vtpm true \
    --output table

echo "✅ VM creada exitosamente"

# Abrir puerto 9000 para SonarQube
echo "🔨 Abriendo puerto 9000 para SonarQube..."
az vm open-port \
    --port 9000 \
    --resource-group $RESOURCE_GROUP \
    --name $VM_NAME \
    --priority 1000

echo "✅ Puerto 9000 abierto"

# Obtener información de la VM
echo "📊 Información de la VM:"
az vm show \
    --resource-group $RESOURCE_GROUP \
    --name $VM_NAME \
    --show-details \
    --query "{name:name, powerState:powerState, publicIps:publicIps, location:location}" \
    --output table

echo ""
echo "🎉 VM creada exitosamente!"
echo "⏭️  Siguiente paso: Ejecutar ./02-install-sonarqube.sh"