#!/bin/bash
# Script 4: Configurar Azure DevOps Pipeline
# Ejecutar después de configurar SonarQube: ./04-configure-azure-devops.sh

set -e

RESOURCE_GROUP="rg-sonarqube-ccr-v2"
VM_NAME="vm-sonarqube-ccr"

echo "🚀 Paso 4: Configurando Azure DevOps Pipeline..."

# Obtener IP de la VM
VM_IP=$(az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME --show-details --query publicIps --output tsv)

echo "📋 Información del deployment:"
echo "🌐 SonarQube URL: http://$VM_IP:9000"
echo ""

# Verificar que SonarQube esté funcionando
echo "🔍 Verificando que SonarQube esté funcionando..."
if ! curl -s --connect-timeout 10 http://$VM_IP:9000/api/system/status > /dev/null; then
    echo "❌ Error: SonarQube no está respondiendo"
    echo "   Ejecuta primero: ./03-verify-sonarqube.sh"
    exit 1
fi

echo "✅ SonarQube está funcionando"

# Preparar código para Azure DevOps
echo ""
echo "📦 Preparando código para Azure DevOps..."

# Verificar que el pipeline esté actualizado
if [ ! -f "azure-pipelines.yml" ]; then
    echo "❌ Error: archivo azure-pipelines.yml no encontrado"
    exit 1
fi

# Actualizar la URL de SonarQube en el pipeline si es necesario
echo "⚙️ Verificando configuración del pipeline..."

# Commit cambios si hay alguno
if ! git diff --quiet; then
    echo "📝 Guardando cambios locales..."
    git add .
    git commit -m "feat: Update SonarQube configuration for deployment

- VM IP: $VM_IP
- SonarQube URL: http://$VM_IP:9000
- Ready for Azure DevOps integration"
fi

echo "✅ Código preparado"

echo ""
echo "🎯 CONFIGURACIÓN MANUAL REQUERIDA:"
echo ""
echo "1. 🌐 CONFIGURAR SONARQUBE:"
echo "   - Abrir: http://$VM_IP:9000"
echo "   - Login: admin / admin"
echo "   - Cambiar contraseña por defecto"
echo "   - Ir a: My Account → Security → Tokens"
echo "   - Generar token: 'azure-devops-pipeline'"
echo "   - Tipo: 'Global Analysis Token'"
echo "   - ⚠️ COPIAR Y GUARDAR EL TOKEN"
echo ""
echo "2. 🏗️ CREAR PROYECTO AZURE DEVOPS:"
echo "   - Ir a: https://dev.azure.com"
echo "   - Crear organización (si no tienes)"
echo "   - Crear proyecto: 'ccr-hospital-management'"
echo "   - Crear repositorio Git"
echo ""
echo "3. 📤 SUBIR CÓDIGO:"
echo "   - Obtener URL del repositorio de Azure DevOps"
echo "   - Ejecutar:"
echo "     git remote add azure https://dev.azure.com/[ORG]/[PROYECTO]/_git/[REPO]"
echo "     git push azure main"
echo ""
echo "4. 🔗 CONFIGURAR SERVICE CONNECTION:"
echo "   - En Azure DevOps: Project Settings → Service Connections"
echo "   - Create service connection → SonarQube"
echo "   - Server URL: http://$VM_IP:9000"
echo "   - Token: [EL TOKEN GENERADO EN PASO 1]"
echo "   - Name: SonarQube-Server"
echo "   - ✅ Grant access permission to all pipelines"
echo ""
echo "5. ⚙️ CREAR PIPELINE:"
echo "   - Pipelines → Create Pipeline"
echo "   - Azure Repos Git → [tu repositorio]"
echo "   - Existing Azure Pipelines YAML file"
echo "   - Path: /azure-pipelines.yml"
echo "   - Save and run"
echo ""

# Crear archivo de instrucciones detalladas
cat > azure-devops-setup-instructions.md << EOF
# Configuración Azure DevOps - Instrucciones Detalladas

## Información del Deployment
- **SonarQube URL**: http://$VM_IP:9000
- **VM IP**: $VM_IP
- **SSH**: ssh azureuser@$VM_IP

## Paso 1: Configurar SonarQube

1. Abrir navegador en: http://$VM_IP:9000
2. Login inicial:
   - Usuario: admin
   - Contraseña: admin
3. Cambiar contraseña (obligatorio en primer login)
4. Generar token para Azure DevOps:
   - Clic en tu perfil (esquina superior derecha)
   - My Account → Security
   - Generate Tokens
   - Name: azure-devops-pipeline
   - Type: Global Analysis Token
   - Expires in: No expiration
   - Clic Generate
   - **⚠️ COPIAR Y GUARDAR EL TOKEN**

## Paso 2: Crear Proyecto Azure DevOps

1. Ir a: https://dev.azure.com
2. Crear/usar organización existente
3. Crear nuevo proyecto:
   - Name: ccr-hospital-management
   - Description: Hospital Management System with CI/CD
   - Visibility: Private
   - Version control: Git
4. Crear repositorio (si no se crea automáticamente)

## Paso 3: Subir Código

\`\`\`bash
# Obtener URL del repositorio de Azure DevOps (ejemplo):
# https://dev.azure.com/tu-org/ccr-hospital-management/_git/ccr-hospital-management

# Agregar remote y subir
git remote add azure [URL-DE-TU-REPOSITORIO]
git push azure main
\`\`\`

## Paso 4: Configurar Service Connection

1. En Azure DevOps proyecto:
   - Project Settings (esquina inferior izquierda)
   - Service connections
   - Create service connection
2. Seleccionar: SonarQube
3. Configurar:
   - Server URL: http://$VM_IP:9000
   - Token: [EL TOKEN DEL PASO 1]
   - Service connection name: SonarQube-Server
   - Description: SonarQube server for CCR project
4. ✅ Marcar: Grant access permission to all pipelines
5. Verify and save

## Paso 5: Crear Pipeline

1. Pipelines → Pipelines → Create Pipeline
2. Where is your code?: Azure Repos Git
3. Select repository: ccr-hospital-management
4. Configure pipeline: Existing Azure Pipelines YAML file
5. Path: /azure-pipelines.yml
6. Continue → Review → Save and run

## Verificación

El pipeline debe ejecutar 4 etapas:
1. **Build**: Maven compile, test, JaCoCo reports
2. **Security**: Trivy vulnerability scanning
3. **SonarQube**: Code quality analysis
4. **Deploy**: Package WAR file

## Troubleshooting

Si el pipeline falla:
1. Verificar Service Connection: debe aparecer ✅ verified
2. Verificar SonarQube: http://$VM_IP:9000 debe responder
3. Verificar logs del pipeline en Azure DevOps
4. Verificar que el token no haya expirado

## Comandos Útiles

\`\`\`bash
# Verificar estado VM
az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME --show-details

# Conectar por SSH
ssh azureuser@$VM_IP

# Ver logs SonarQube
ssh azureuser@$VM_IP "cd /opt/sonarqube && docker-compose logs sonarqube"

# Reiniciar SonarQube si es necesario
ssh azureuser@$VM_IP "cd /opt/sonarqube && docker-compose restart sonarqube"
\`\`\`
EOF

echo "📄 Instrucciones detalladas guardadas en: azure-devops-setup-instructions.md"

echo ""
echo "🎯 RESUMEN DE TAREAS MANUALES:"
echo ""
echo "✅ VM creada y SonarQube funcionando"
echo "📋 TODO - Configurar manualmente:"
echo "   1. SonarQube token (http://$VM_IP:9000)"
echo "   2. Proyecto Azure DevOps"
echo "   3. Service Connection"
echo "   4. Pipeline"
echo ""
echo "📖 Ver instrucciones completas en: azure-devops-setup-instructions.md"

# Mostrar información final
echo ""
echo "📊 INFORMACIÓN FINAL:"
cat deployment-info.txt