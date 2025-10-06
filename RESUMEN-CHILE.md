# 🇨🇱 Configuración Optimizada para Chile

## ✅ Optimizaciones Implementadas

### 📍 **Región: Chile Central**
- Ubicación: `chilecentral`
- Menor latencia para usuarios en Chile
- Cumplimiento de residencia de datos en Chile

### 💻 **VM Económica: Standard_B1ms**
- **vCPUs:** 1
- **RAM:** 2GB (mínimo requerido para SonarQube)
- **Costo:** ~$15-20 USD/mes
- **Ideal para:** Proyectos académicos y desarrollo

### 🚀 **SonarQube Optimizado**
- Configuración de memoria específica para 2GB RAM
- Límites de memoria por proceso:
  - **Compute Engine:** 512MB
  - **Web Server:** 512MB
  - **Elasticsearch:** 512MB
- **Límite total del contenedor:** 1.5GB (deja 500MB para el SO)

## 💰 **Análisis de Costos**

### Costos Mensuales (24/7)
```
VM Standard_B1ms:     $15-20
Almacenamiento SSD:   $3-5
IP Pública:           $3
Tráfico de Red:       $1-2
-------------------------
TOTAL:                $22-30/mes
```

### Costos Optimizados (8h/día, 5 días/semana)
```
VM (160h/mes):        $5-8
Almacenamiento:       $3-5
IP Pública:           $3
Tráfico:              $1
-------------------------
TOTAL:                $12-17/mes
```

## ⚡ **Comandos de Gestión de Costos**

### Detener VM (ahorrar ~70% en cómputo)
```bash
az vm stop --resource-group rg-sonarqube-ccr --name vm-sonarqube-ccr
```

### Iniciar VM cuando necesites usarla
```bash
az vm start --resource-group rg-sonarqube-ccr --name vm-sonarqube-ccr
```

### Programar apagado automático
```bash
# Apagar a las 6 PM (útil para proyectos académicos)
az vm auto-shutdown --resource-group rg-sonarqube-ccr --name vm-sonarqube-ccr --time 1800
```

## 🎯 **Pipeline Optimizado para Recursos Limitados**

### Ajustes en el Pipeline
- **Maven:** Compilación optimizada con `-q` (quiet mode)
- **Trivy:** Scans específicos sin overhead
- **SonarQube:** Timeouts ajustados para VM pequeña

### Métricas Esperadas
- **Tiempo de build:** 3-5 minutos
- **Análisis SonarQube:** 2-4 minutos
- **Scan de Trivy:** 1-2 minutos
- **Total pipeline:** 6-11 minutos

## 🔧 **Monitoreo de Recursos**

### Verificar uso de memoria en la VM
```bash
ssh azureuser@[IP-VM]
free -h
docker stats sonarqube
```

### Ver logs si hay problemas de memoria
```bash
ssh azureuser@[IP-VM]
cd /opt/sonarqube
sudo docker-compose logs sonarqube | grep -i "memory\|heap\|gc"
```

## 📊 **Benchmarks Esperados**

### Para el proyecto CCR Hospital Management:
- **Líneas de código:** ~3,000-5,000
- **Tiempo de análisis:** 2-3 minutos
- **Uso de memoria:** 60-80% de 1.5GB disponible
- **Vulnerabilidades detectadas:** Variable según dependencias

## 🚨 **Señales de Alerta**

### Memoria Insuficiente
```
SÍNTOMAS:
- SonarQube se reinicia constantemente
- Pipeline falla en stage de SonarQube
- Logs muestran "OutOfMemoryError"

SOLUCIÓN:
- Upgrade a Standard_B2s (4GB RAM)
- Comando: az vm resize --resource-group rg-sonarqube-ccr --name vm-sonarqube-ccr --size Standard_B2s
```

### Performance Lenta
```
SÍNTOMAS:
- Pipeline toma >15 minutos
- SonarQube responde lento

SOLUCIÓN:
- Verificar que no hay otros contenedores corriendo
- Considerar upgrade temporal para análisis grandes
```

## 🎓 **Tips para Estudiantes**

### Uso Eficiente
1. **Apagar VM** después de cada sesión de trabajo
2. **Usar horarios definidos** para desarrollo (ej: 9am-6pm)
3. **Compartir VM** entre compañeros de equipo si es posible
4. **Eliminar recursos** al final del semestre

### Comando de Limpieza Total
```bash
# ⚠️ CUIDADO: Elimina TODOS los recursos
az group delete --name rg-sonarqube-ccr --yes --no-wait
```

## 📱 **Acceso Móvil**

SonarQube es responsive, puedes revisar resultados desde móvil:
- URL: `http://[IP-VM]:9000`
- Dashboard mobile-friendly
- Notificaciones por email configurables

---

## ✅ **Checklist de Validación**

- [ ] VM en región `chilecentral`
- [ ] Tamaño `Standard_B1ms` (2GB RAM)
- [ ] SonarQube con límites de memoria configurados
- [ ] Pipeline ejecuta en <15 minutos
- [ ] Costos proyectados <$30/mes
- [ ] Scripts de start/stop funcionando
- [ ] Documentación actualizada

**🚀 ¡Tu setup está optimizado para Chile y es cost-effective!**