# Local SonarQube Setup

Este setup permite ejecutar SonarQube localmente usando Docker y analizar el proyecto sin necesidad de la VM de Azure.

## Prerrequisitos

- Docker Desktop instalado y ejecutándose
- Maven instalado
- Java 17 o superior

## Uso Rápido

### 1. Iniciar SonarQube Local
```bash
docker-compose up -d
```

### 2. Ejecutar Pipeline Completo
```bash
./run-local-pipeline.sh
```

### 3. Ver Resultados
- **SonarQube Dashboard:** http://localhost:9000
- **Login inicial:** admin/admin

## Comandos Útiles

### Detener SonarQube
```bash
docker-compose down
```

### Detener y eliminar datos
```bash
docker-compose down -v
```

### Solo análisis SonarQube (sin Docker setup)
```bash
cd ccrHospitalManagement
mvn clean compile test jacoco:report
mvn sonar:sonar \
  -Dsonar.projectKey=ccrHospitalManagement-local \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=tu_token_aqui
```

## Estructura de Archivos

- `docker-compose.yml` - Configuración de SonarQube + PostgreSQL
- `run-local-pipeline.sh` - Script completo de pipeline
- Este README para instrucciones

## Componentes

- **SonarQube 10.3 Community** en puerto 9000
- **PostgreSQL 15** como base de datos
- **Volúmenes persistentes** para datos
- **Pipeline automatizado** con Maven + JaCoCo