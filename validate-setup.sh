#!/bin/bash

# Script de validación para verificar el setup completo
# Verifica que todos los componentes estén funcionando correctamente

set -e

echo "🔍 Validando Setup de SonarQube + Trivy Pipeline"
echo "=============================================="

# Colores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Contadores
TESTS_PASSED=0
TESTS_FAILED=0

# Función para tests
run_test() {
    local test_name="$1"
    local test_command="$2"

    echo -n "🧪 $test_name... "

    if eval "$test_command" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ PASSED${NC}"
        ((TESTS_PASSED++))
        return 0
    else
        echo -e "${RED}❌ FAILED${NC}"
        ((TESTS_FAILED++))
        return 1
    fi
}

# Función para tests con output
run_test_with_output() {
    local test_name="$1"
    local test_command="$2"

    echo "🧪 $test_name..."

    if eval "$test_command"; then
        echo -e "${GREEN}✅ PASSED${NC}"
        ((TESTS_PASSED++))
        return 0
    else
        echo -e "${RED}❌ FAILED${NC}"
        ((TESTS_FAILED++))
        return 1
    fi
}

echo ""
echo "1️⃣ Verificando prerrequisitos..."

run_test "Azure CLI instalado" "command -v az"
run_test "Git instalado" "command -v git"
run_test "Azure login activo" "az account show"

echo ""
echo "2️⃣ Verificando archivos del proyecto..."

run_test "Pipeline YAML existe" "test -f azure-pipelines.yml"
run_test "Script de deploy existe" "test -f deploy-sonarqube-vm.sh"
run_test "Documentación existe" "test -f SONARQUBE-SETUP.md"
run_test "POM.xml del proyecto existe" "test -f ccrHospitalManagement/pom.xml"

echo ""
echo "3️⃣ Verificando configuración del proyecto..."

run_test "SonarQube plugin en POM" "grep -q 'sonar-maven-plugin' ccrHospitalManagement/pom.xml"
run_test "JaCoCo plugin en POM" "grep -q 'jacoco-maven-plugin' ccrHospitalManagement/pom.xml"
run_test "Pipeline tiene stages correctos" "grep -q 'SecurityScan' azure-pipelines.yml && grep -q 'SonarQubeAnalysis' azure-pipelines.yml"

echo ""
echo "4️⃣ Verificando recursos de Azure (si existen)..."

RESOURCE_GROUP="rg-sonarqube-ccr"
VM_NAME="vm-sonarqube-ccr"

if az group show --name $RESOURCE_GROUP > /dev/null 2>&1; then
    echo "   📦 Grupo de recursos encontrado: $RESOURCE_GROUP"

    run_test "VM existe" "az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME"

    if az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME > /dev/null 2>&1; then
        VM_IP=$(az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME --show-details --query publicIps --output tsv)
        echo "   🌐 IP de la VM: $VM_IP"

        VM_STATE=$(az vm show --resource-group $RESOURCE_GROUP --name $VM_NAME --show-details --query powerState --output tsv)
        echo "   🔋 Estado de la VM: $VM_STATE"

        if [[ "$VM_STATE" == "VM running" ]]; then
            echo ""
            echo "5️⃣ Verificando SonarQube..."

            # Test de conectividad a SonarQube
            if curl -s -f "http://$VM_IP:9000/api/system/status" > /dev/null 2>&1; then
                echo -e "   🌐 SonarQube respondiendo en http://$VM_IP:9000 ${GREEN}✅${NC}"
                ((TESTS_PASSED++))

                # Verificar version de SonarQube
                SONAR_VERSION=$(curl -s "http://$VM_IP:9000/api/system/status" | grep -o '"version":"[^"]*"' | cut -d'"' -f4)
                echo "   📊 Versión de SonarQube: $SONAR_VERSION"

            else
                echo -e "   🌐 SonarQube NO responde en http://$VM_IP:9000 ${RED}❌${NC}"
                echo "      💡 Tip: Espera 2-3 minutos o verifica que la VM esté iniciada"
                ((TESTS_FAILED++))
            fi
        else
            echo -e "   ⚠️  VM no está ejecutándose. Estado: $VM_STATE ${YELLOW}⚠️${NC}"
            echo "      Para iniciarla: az vm start --resource-group $RESOURCE_GROUP --name $VM_NAME"
        fi
    fi
else
    echo "   📦 No se encontró grupo de recursos de Azure"
    echo "      💡 Ejecuta './setup-complete-pipeline.sh' para crear la infraestructura"
fi

echo ""
echo "6️⃣ Verificando herramientas de build..."

if command -v mvn > /dev/null 2>&1; then
    echo "   ☕ Maven instalado: $(mvn -version | head -1)"
    run_test "Proyecto compila" "cd ccrHospitalManagement && mvn compile -q"
else
    echo "   ⚠️  Maven no está instalado (no es crítico para el pipeline)"
fi

if command -v java > /dev/null 2>&1; then
    echo "   ☕ Java instalado: $(java -version 2>&1 | head -1)"
else
    echo "   ⚠️  Java no está instalado localmente (no es crítico para el pipeline)"
fi

echo ""
echo "7️⃣ Verificando configuración de archivos..."

# Verificar configuración específica en archivos
run_test "Pipeline usa Java 17" "grep -q 'versionSpec.*17' azure-pipelines.yml"
run_test "Pipeline incluye Trivy" "grep -q 'trivy' azure-pipelines.yml"
run_test "Pipeline publica artefactos" "grep -q 'PublishBuildArtifacts' azure-pipelines.yml"

echo ""
echo "==================== RESUMEN ===================="

TOTAL_TESTS=$((TESTS_PASSED + TESTS_FAILED))
SUCCESS_RATE=$((TESTS_PASSED * 100 / TOTAL_TESTS))

echo "📊 Tests ejecutados: $TOTAL_TESTS"
echo -e "✅ Exitosos: ${GREEN}$TESTS_PASSED${NC}"
echo -e "❌ Fallidos: ${RED}$TESTS_FAILED${NC}"
echo -e "📈 Tasa de éxito: ${GREEN}$SUCCESS_RATE%${NC}"

echo ""

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}🎉 ¡VALIDACIÓN EXITOSA! Todo está configurado correctamente.${NC}"
    echo ""
    echo "✅ Próximos pasos:"
    echo "   1. Sube el código a Azure DevOps"
    echo "   2. Configura el Service Connection para SonarQube"
    echo "   3. Crea el pipeline usando azure-pipelines.yml"
    echo "   4. ¡Ejecuta tu primer pipeline!"

    if [ ! -z "$VM_IP" ]; then
        echo ""
        echo "🔗 Enlaces importantes:"
        echo "   • SonarQube: http://$VM_IP:9000"
        echo "   • Documentación: SONARQUBE-SETUP.md"
        echo "   • Pasos siguientes: NEXT-STEPS.md"
    fi

    exit 0
else
    echo -e "${YELLOW}⚠️  VALIDACIÓN PARCIAL. Algunos tests fallaron.${NC}"
    echo ""
    echo "🔧 Acciones recomendadas:"
    echo "   • Revisa los errores específicos arriba"
    echo "   • Consulta SONARQUBE-SETUP.md para troubleshooting"
    echo "   • Ejecuta './setup-complete-pipeline.sh' si faltan recursos"

    exit 1
fi