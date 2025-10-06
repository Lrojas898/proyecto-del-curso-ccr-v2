# Implementación de SonarQube con Azure DevOps Pipeline + Trivy

Este proyecto implementa un pipeline completo de CI/CD que incluye análisis de calidad de código con SonarQube y análisis de seguridad con Trivy, desplegado en Azure.

## 🏗️ Arquitectura

```
Azure DevOps Pipeline
├── Build & Test (Maven + JUnit)
├── Security Scan (Trivy)
├── Code Quality (SonarQube)
└── Package (WAR)
         ↓
Azure VM con SonarQube
```

## 📋 Prerrequisitos

1. **Azure CLI** instalado y configurado
2. **Cuenta de Azure DevOps**
3. **Suscripción de Azure** activa
4. **Permisos** para crear recursos en Azure

## 🚀 Pasos de Implementación

### Paso 1: Desplegar SonarQube en Azure VM

```bash
# Hacer el script ejecutable
chmod +x deploy-sonarqube-vm.sh

# Ejecutar el despliegue
./deploy-sonarqube-vm.sh
```

Este script:
- ✅ Crea un grupo de recursos en Azure
- ✅ Despliega una VM Ubuntu con Docker
- ✅ Instala y configura SonarQube
- ✅ Configura reglas de firewall necesarias

### Paso 2: Configurar SonarQube

1. **Acceder a SonarQube:**
   - URL: `http://[IP-PUBLICA]:9000`
   - Usuario: `admin`
   - Contraseña: `admin`

2. **Cambiar contraseña por defecto**

3. **Generar token para pipeline:**
   - Ir a: My Account → Security → Generate Tokens
   - Nombre: `azure-devops-pipeline`
   - Copiar el token generado

### Paso 3: Configurar Azure DevOps

#### 3.1 Crear/Importar Repositorio

```bash
# Opción 1: Clonar este repositorio
git clone [URL_DE_TU_REPO]

# Opción 2: Inicializar nuevo repositorio
git init
git add .
git commit -m "Initial commit with SonarQube pipeline"
git remote add origin [URL_AZURE_DEVOPS_REPO]
git push -u origin main
```

#### 3.2 Configurar Service Connection

1. En Azure DevOps → Project Settings → Service connections
2. Crear nueva conexión → SonarQube
3. Configurar:
   - **Server URL:** `http://[IP-PUBLICA]:9000`
   - **Token:** El token generado en el paso anterior
   - **Service connection name:** `SonarQube-Server`

#### 3.3 Crear Pipeline

1. Azure DevOps → Pipelines → New pipeline
2. Seleccionar tu repositorio
3. Usar el archivo `azure-pipelines.yml` existente

### Paso 4: Ejecutar Pipeline

El pipeline ejecutará automáticamente:

1. **🔨 Build Stage:**
   - Compilación con Maven
   - Ejecución de pruebas unitarias
   - Generación de reporte JaCoCo

2. **🔐 Security Stage:**
   - Análisis de vulnerabilidades con Trivy
   - Detección de secretos
   - Publicación de reportes SARIF

3. **📊 SonarQube Stage:**
   - Análisis estático de código
   - Cálculo de métricas de calidad
   - Envío de resultados a SonarQube

4. **📦 Deploy Stage:**
   - Empaquetado de aplicación WAR
   - Publicación de artefactos

## 📊 Métricas y Reportes

### SonarQube Dashboard
- **Cobertura de código:** Métricas de testing
- **Duplicación:** Código duplicado
- **Mantenibilidad:** Code smells y deuda técnica
- **Confiabilidad:** Bugs detectados
- **Seguridad:** Vulnerabilidades de código

### Trivy Reports
- **Vulnerabilidades:** CVEs en dependencias
- **Secretos:** Credenciales hardcodeadas
- **Configuraciones:** Malas prácticas de seguridad

## 🛠️ Comandos Útiles

### Gestión de la VM

```bash
# Ver estado de la VM
az vm show --resource-group rg-sonarqube-ccr --name vm-sonarqube-ccr

# Iniciar VM
az vm start --resource-group rg-sonarqube-ccr --name vm-sonarqube-ccr

# Detener VM
az vm stop --resource-group rg-sonarqube-ccr --name vm-sonarqube-ccr

# Conectar por SSH
ssh azureuser@[IP-PUBLICA]
```

### Gestión de SonarQube

```bash
# Conectar a la VM y verificar SonarQube
ssh azureuser@[IP-PUBLICA]
cd /opt/sonarqube
sudo docker-compose ps
sudo docker-compose logs sonarqube
```

### Pipeline Local (Testing)

```bash
# Ejecutar análisis local
cd ccrHospitalManagement
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=ccrHospitalManagement \
  -Dsonar.host.url=http://[IP-PUBLICA]:9000 \
  -Dsonar.token=[TU-TOKEN]

# Ejecutar Trivy localmente
trivy fs --format table .
trivy fs --format sarif --output trivy-results.sarif .
```

## 🔧 Troubleshooting

### Error: SonarQube no responde
```bash
# Verificar estado del contenedor
sudo docker ps
sudo docker logs sonarqube

# Reiniciar SonarQube
sudo docker-compose restart
```

### Error: Pipeline falla en SonarQube step
1. Verificar que el Service Connection esté configurado correctamente
2. Verificar que el token sea válido
3. Verificar conectividad de red

### Error: Trivy no encuentra vulnerabilidades
- Es normal si el código no tiene dependencias vulnerables
- Verificar que el scan se ejecute correctamente en los logs

## 💰 Costos Estimados (Región Chile Central)

- **VM Standard_B1ms (1 vCPU, 2GB RAM):** ~$15-20 USD/mes
- **Almacenamiento SSD:** ~$3-5 USD/mes
- **Tráfico de red:** Mínimo para testing (~$1-2 USD/mes)
- **IP pública:** ~$3 USD/mes

**Total estimado:** ~$22-30 USD/mes

> 💡 **Tip de ahorro:** Detener la VM cuando no esté en uso puede reducir costos a ~$8-12 USD/mes (solo almacenamiento + IP)

## 🧹 Limpieza de Recursos

```bash
# Eliminar todo el grupo de recursos
az group delete --name rg-sonarqube-ccr --yes --no-wait
```

## 📚 Referencias

- [SonarQube Documentation](https://docs.sonarqube.org/)
- [Trivy Documentation](https://aquasecurity.github.io/trivy/)
- [Azure DevOps Pipelines](https://docs.microsoft.com/en-us/azure/devops/pipelines/)
- [Azure VM Documentation](https://docs.microsoft.com/en-us/azure/virtual-machines/)

---

**📝 Nota:** Este setup está optimizado para fines educativos y testing. Para producción, considera usar Azure Container Instances o App Service con configuración de alta disponibilidad.