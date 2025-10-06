#!/bin/bash

# Script completo para configurar Pipeline de SonarQube + Trivy en Azure
# Automatiza todo el proceso de setup

set -e

echo "🎯 CCR Hospital Management - Pipeline Setup"
echo "=========================================="
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Funciones auxiliares
print_step() {
    echo -e "${BLUE}📋 $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Verificar prerrequisitos
print_step "Verificando prerrequisitos..."

# Verificar Azure CLI
if ! command -v az &> /dev/null; then
    print_error "Azure CLI no está instalado. Instálalo desde: https://docs.microsoft.com/en-us/cli/azure/install-azure-cli"
    exit 1
fi

# Verificar login de Azure
if ! az account show &> /dev/null; then
    print_error "No estás autenticado en Azure. Ejecuta: az login"
    exit 1
fi

# Verificar Git
if ! command -v git &> /dev/null; then
    print_error "Git no está instalado."
    exit 1
fi

print_success "Prerrequisitos verificados"

# Obtener información de la cuenta
SUBSCRIPTION_NAME=$(az account show --query name --output tsv)
SUBSCRIPTION_ID=$(az account show --query id --output tsv)

echo ""
echo "📊 Información de la suscripción:"
echo "   Nombre: $SUBSCRIPTION_NAME"
echo "   ID: $SUBSCRIPTION_ID"
echo ""

# Confirmar continuación
read -p "¿Continuar con el despliegue? (y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Despliegue cancelado."
    exit 1
fi

# Variables de configuración
RESOURCE_GROUP="rg-sonarqube-ccr"
PROJECT_NAME="ccr-hospital-management"

print_step "Desplegando infraestructura de Azure..."

# Ejecutar script de despliegue de VM
if [ -f "deploy-sonarqube-vm.sh" ]; then
    chmod +x deploy-sonarqube-vm.sh
    ./deploy-sonarqube-vm.sh

    if [ $? -eq 0 ]; then
        print_success "VM de SonarQube desplegada exitosamente"
    else
        print_error "Error al desplegar la VM"
        exit 1
    fi
else
    print_error "Archivo deploy-sonarqube-vm.sh no encontrado"
    exit 1
fi

# Obtener IP de la VM
VM_IP=$(az vm show \
    --resource-group $RESOURCE_GROUP \
    --name vm-sonarqube-ccr \
    --show-details \
    --query publicIps \
    --output tsv)

print_step "Esperando a que SonarQube esté completamente listo..."
echo "🔄 Esto puede tomar 2-3 minutos..."

# Esperar a que SonarQube responda
MAX_ATTEMPTS=30
ATTEMPT=1
while [ $ATTEMPT -le $MAX_ATTEMPTS ]; do
    if curl -s -f "http://$VM_IP:9000/api/system/status" > /dev/null 2>&1; then
        print_success "SonarQube está respondiendo"
        break
    fi

    echo "   Intento $ATTEMPT/$MAX_ATTEMPTS - Esperando..."
    sleep 10
    ((ATTEMPT++))
done

if [ $ATTEMPT -gt $MAX_ATTEMPTS ]; then
    print_warning "SonarQube puede no estar completamente listo. Verifica manualmente."
fi

# Inicializar repositorio Git si no existe
if [ ! -d ".git" ]; then
    print_step "Inicializando repositorio Git..."
    git init
    git add .
    git commit -m "Initial commit: SonarQube pipeline setup with Trivy integration

Features:
- Azure DevOps pipeline with Maven build
- SonarQube integration for code quality analysis
- Trivy integration for security scanning
- JaCoCo code coverage reports
- Automated deployment scripts"

    print_success "Repositorio Git inicializado"
fi

# Crear archivo de configuración para el usuario
cat > pipeline-config.env <<EOF
# Configuración del Pipeline CCR Hospital Management
# ================================================

# Información de la VM SonarQube
SONARQUBE_VM_IP=$VM_IP
SONARQUBE_URL=http://$VM_IP:9000
SONARQUBE_DEFAULT_USER=admin
SONARQUBE_DEFAULT_PASS=admin

# Información de Azure
AZURE_RESOURCE_GROUP=$RESOURCE_GROUP
AZURE_SUBSCRIPTION_ID=$SUBSCRIPTION_ID

# Configuración del Pipeline
PROJECT_NAME=$PROJECT_NAME
PIPELINE_FILE=azure-pipelines.yml

# URLs importantes
SONARQUBE_DASHBOARD=http://$VM_IP:9000/projects
AZURE_DEVOPS_PIPELINES=https://dev.azure.com/[TU-ORGANIZACION]/[TU-PROYECTO]/_build

# Próximos pasos (completar manualmente):
# 1. Acceder a SonarQube y cambiar contraseña: $SONARQUBE_URL
# 2. Generar token en SonarQube: My Account → Security → Generate Tokens
# 3. Crear Service Connection en Azure DevOps con el token
# 4. Subir código a Azure DevOps y crear pipeline
EOF

# Crear checklist para el usuario
cat > NEXT-STEPS.md <<EOF
# ✅ Próximos Pasos - Checklist

## 1. Configurar SonarQube (5 minutos)

- [ ] Acceder a SonarQube: http://$VM_IP:9000
- [ ] Login con usuario \`admin\` y contraseña \`admin\`
- [ ] **IMPORTANTE:** Cambiar contraseña por defecto
- [ ] Ir a: My Account → Security → Generate Tokens
- [ ] Crear token con nombre: \`azure-devops-pipeline\`
- [ ] **Copiar y guardar el token** (lo necesitarás para Azure DevOps)

## 2. Configurar Azure DevOps (10 minutos)

### 2.1 Subir código a Azure DevOps
\`\`\`bash
# Si no tienes repositorio en Azure DevOps, créalo primero
git remote add origin https://dev.azure.com/[TU-ORG]/[TU-PROYECTO]/_git/[REPO-NAME]
git push -u origin main
\`\`\`

### 2.2 Crear Service Connection
- [ ] Azure DevOps → Project Settings → Service connections
- [ ] New service connection → SonarQube
- [ ] Configurar:
  - Server URL: \`http://$VM_IP:9000\`
  - Token: [El token que generaste en SonarQube]
  - Service connection name: \`SonarQube-Server\`

### 2.3 Crear Pipeline
- [ ] Azure DevOps → Pipelines → New pipeline
- [ ] Seleccionar tu repositorio
- [ ] Existing Azure Pipelines YAML file
- [ ] Seleccionar: \`/azure-pipelines.yml\`
- [ ] Save and run

## 3. Verificar Pipeline (5 minutos)

- [ ] Pipeline se ejecuta sin errores
- [ ] Ver resultados en SonarQube Dashboard: http://$VM_IP:9000/projects
- [ ] Verificar reportes de Trivy en Azure DevOps
- [ ] Revisar artifacts generados

## 4. Comandos Útiles

### Gestionar VM
\`\`\`bash
# Ver estado
az vm show --resource-group $RESOURCE_GROUP --name vm-sonarqube-ccr

# Detener VM (para ahorrar costos)
az vm stop --resource-group $RESOURCE_GROUP --name vm-sonarqube-ccr

# Iniciar VM
az vm start --resource-group $RESOURCE_GROUP --name vm-sonarqube-ccr
\`\`\`

### Conectar por SSH
\`\`\`bash
ssh azureuser@$VM_IP
\`\`\`

### Ver logs de SonarQube
\`\`\`bash
ssh azureuser@$VM_IP
cd /opt/sonarqube
sudo docker-compose logs sonarqube
\`\`\`

## 5. Limpiar Recursos (cuando termines)

\`\`\`bash
# CUIDADO: Esto elimina TODOS los recursos
az group delete --name $RESOURCE_GROUP --yes --no-wait
\`\`\`

---

## 🆘 ¿Problemas?

1. **SonarQube no responde:** Espera 5 minutos más o reinicia la VM
2. **Pipeline falla:** Verifica que el Service Connection esté configurado
3. **No hay vulnerabilidades en Trivy:** Es normal, significa que el código es seguro

## 📞 Soporte

Revisa el archivo \`SONARQUBE-SETUP.md\` para documentación completa.
EOF

echo ""
print_success "🎉 ¡Setup completado exitosamente!"
echo ""
echo "📋 Información importante:"
echo "   • SonarQube URL: http://$VM_IP:9000"
echo "   • Usuario inicial: admin / admin"
echo "   • Configuración guardada en: pipeline-config.env"
echo "   • Próximos pasos en: NEXT-STEPS.md"
echo ""
print_warning "🔑 IMPORTANTE: Cambia la contraseña de SonarQube inmediatamente"
print_warning "💰 RECORDATORIO: Detén la VM cuando no la uses para ahorrar costos"
echo ""
echo "🚀 ¡Tu pipeline de SonarQube + Trivy está listo!"