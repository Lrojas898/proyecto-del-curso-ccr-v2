# Actividad en Clase: Sonarqube en Repositorio Github 
LUIS MANUEL ROJAS CORREA A00399289
INGE SOFT V ICESI

*Repositorio Utilizado:* [https://github.com/Computacion-2/proyecto-del-curso-ccr\#](https://github.com/Computacion-2/proyecto-del-curso-ccr#)

Credenciales SonnarQube:
Username: admin
Password: ilovedevops
nota: debe ingresar asegurando que las configuraciones de su web server NO envian request en https, sino en http 

<img width="956" height="1012" alt="image" src="https://github.com/user-attachments/assets/d0ca5392-b20e-4607-b900-f1e957167169" />

SonarQube es una plataforma de código abierto utilizada para la inspección continua de la calidad del código. Funciona como una herramienta de análisis estático que examina el código fuente de un proyecto para detectar bugs, vulnerabilidades de seguridad y malas prácticas de programación. Su objetivo es proporcionar métricas en un panel de control visual para que los equipos de desarrollo puedan mejorar la mantenibilidad, fiabilidad y seguridad de su software a lo largo del ciclo de vida del desarrollo.

## Proceso de Instalación y Aplicación de SonarQube

La integración de SonarQube se realizó configurando el plugin sonar-maven-plugin versión 3.11.0.3922 en el archivo pom.xml del proyecto Spring Boot. Se añadieron las propiedades necesarias para conectar con la instancia local de SonarQube (http://localhost:9000), incluyendo la configuración del proyecto con clave proyecto-del-curso-ccr y la integración con JaCoCo para generar reportes de cobertura de código. La autenticación se configuró mediante un token generado desde la interfaz web de SonarQube, utilizando las credenciales por defecto (admin/admin). El análisis se ejecutó mediante el comando Maven mvn clean verify sonar:sonar con los parámetros correspondientes para subir los resultados al servidor local.

## Resultados del Análisis de SonarQube

El análisis procesó un total de 148 archivos del proyecto, incluyendo 131 archivos Java principales y 16 archivos de prueba. Se ejecutaron 191 pruebas unitarias sin errores ni fallos, obteniendo un 100% de éxito en la suite de testing. JaCoCo generó el reporte de cobertura de código analizando 62 clases del proyecto. El análisis detectó 2 lenguajes (Java y XML) y aplicó los perfiles de calidad "Sonar way" correspondientes. El proceso completo tomó 6 minutos y 30 segundos, incluyendo la compilación, ejecución de pruebas, generación de reportes y análisis estático del código. Los resultados fueron subidos exitosamente al dashboard de SonarQube local, donde se pueden visualizar las métricas de calidad, duplicación de código, vulnerabilidades de seguridad y cobertura de pruebas del proyecto.

# Tarea Miércoles: Despliegue Sonarqube

*Repositorio de GitHub Usado: [https://github.com/Lrojas898/proyecto-del-curso-ccr-v2](https://github.com/Lrojas898/proyecto-del-curso-ccr-v2)*

### **Azure Virtual Machine**

Azure Virtual Machine (VM) es un servicio de Infraestructura como Servicio (IaaS) que ofrece máquinas virtuales escalables. En este proyecto, se desplegó una VM con sistema operativo Linux Ubuntu en la región de Chile Central, asignada al grupo de recursos rg-sonarqube-ccr-v2. Esta VM funciona como host para SonarQube y su configuración de red incluye reglas de seguridad para permitir el tráfico a través de los puertos 22 (SSH) y 9000 (HTTP).


### **SonarQube**

SonarQube es una plataforma para el análisis estático de código que identifica errores, vulnerabilidades y "code smells". Se instaló SonarQube Community Edition 10.3 en la VM de Azure y se configuró un proyecto denominado ccrHospitalManagementV2, para el cual se generó un token de autenticación. La integración con el pipeline de GitHub Actions se estableció mediante las variables de entorno SONAR\_TOKEN y SONAR\_HOST\_URL, permitiendo la ejecución de análisis automáticos del código Java y el reporte de métricas de calidad.

**GitHub Actions Pipeline**

GitHub Actions es una plataforma de Integración Continua y Despliegue Continuo (CI/CD) que automatiza flujos de trabajo mediante archivos de configuración en formato YAML. El pipeline implementado consta de cuatro trabajos secuenciales:

1. **Build and Test:** Compila el proyecto Maven y ejecuta pruebas unitarias, generando informes de cobertura con JaCoCo.  
2. **Security Analysis:** Realiza escaneos de seguridad utilizando Trivy.  
3. **SonarQube Analysis:** Envía el código fuente a SonarQube para su análisis de calidad.  
4. **Package:** Genera los artefactos de la aplicación en formato WAR.

Detallando de una mejor forma la parte de sonnarQube: Ejecuta la compilación y pruebas del proyecto para generar los artefactos necesarios (clases compiladas y reportes de cobertura JaCoCo), luego invoca el plugin Maven de SonarQube que empaqueta el código fuente, resultados de pruebas, métricas de cobertura y metadatos del proyecto. Esta información se transmite a la instancia de SonarQube ejecutándose en la VM de Azure mediante autenticación por token, donde el servidor procesa el análisis estático del código Java, calcula métricas de calidad, identifica bugs y code smells, y almacena los resultados en su base de datos para consulta posterior a través de la interfaz web



### **Trivy**

Trivy es un escáner de vulnerabilidades para contenedores, sistemas de archivos y repositorios Git. Dentro del pipeline, Trivy analiza el directorio del proyecto para identificar vulnerabilidades (CVEs) en las dependencias de Maven especificadas en el archivo pom.xml y detectar secretos expuestos, como tokens o credenciales. Los resultados se generan en formato SARIF y se integran con GitHub Security, clasificando los hallazgos por severidad (alta, media, baja) para su posterior seguimiento en la pestaña de seguridad del repositorio.
