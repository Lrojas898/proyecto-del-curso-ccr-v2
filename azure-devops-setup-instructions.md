# Configuración Azure DevOps - Instrucciones Detalladas

## Información del Deployment
- **SonarQube URL**: http://68.211.121.135:9000
- **VM IP**: 68.211.121.135
- **SSH**: ssh azureuser@68.211.121.135

## Paso 1: Configurar SonarQube

1. Abrir navegador en: http://68.211.121.135:9000
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

```bash
# Obtener URL del repositorio de Azure DevOps (ejemplo):
# https://dev.azure.com/tu-org/ccr-hospital-management/_git/ccr-hospital-management

# Agregar remote y subir
git remote add azure [URL-DE-TU-REPOSITORIO]
git push azure main
```

## Paso 4: Configurar Service Connection

1. En Azure DevOps proyecto:
   - Project Settings (esquina inferior izquierda)
   - Service connections
   - Create service connection
2. Seleccionar: SonarQube
3. Configurar:
   - Server URL: http://68.211.121.135:9000
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
2. Verificar SonarQube: http://68.211.121.135:9000 debe responder
3. Verificar logs del pipeline en Azure DevOps
4. Verificar que el token no haya expirado

## Comandos Útiles

```bash
# Verificar estado VM
az vm show --resource-group rg-sonarqube-ccr-v2 --name vm-sonarqube-ccr --show-details

# Conectar por SSH
ssh azureuser@68.211.121.135

# Ver logs SonarQube
ssh azureuser@68.211.121.135 "cd /opt/sonarqube && docker-compose logs sonarqube"

# Reiniciar SonarQube si es necesario
ssh azureuser@68.211.121.135 "cd /opt/sonarqube && docker-compose restart sonarqube"
```
