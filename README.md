[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/2S86ERbh)
# Proyecto - Clinica OncoLogic IPS

## Integrantes:

- Ricardo Andres Chamorro Martinez

- Luis Manuel Rojas Correa

- Juan Camilo Corrales Osvalth

## Propuesta de modelo.

[Ver diagrama relacional (PDF)](docs/RELATIONAL%20DIAGRAM.pdf)

## Contexto del Proyecto

En la ciudad de **Cali**, una nueva **clínica especializada en oncología** ha sido inaugurada con el objetivo de ofrecer atención médica de alta calidad a sus pacientes. A pesar de contar con un personal médico altamente especializado y la infraestructura necesaria, la clínica aún no dispone de un sistema digital que facilite la gestión de sus actividades administrativas y operativas.

Con el fin de optimizar los procesos internos y mejorar la atención al paciente, la clínica requiere una plataforma web integral que permita gestionar de manera eficiente varias áreas críticas de su operación. Esta plataforma debe cubrir desde el manejo de personal médico y administrativo, hasta la programación de citas médicas, gestión de historiales clínicos y la integración de módulos de laboratorio.

## Objetivo del Proyecto

Desarrollar una **aplicación web integral** para la **gestión administrativa y operativa** de la clínica oncológica. La solución permitirá un control eficiente sobre las citas médicas, los expedientes clínicos, el personal médico, los laboratorios y la facturación. A través de esta plataforma, se garantizará un flujo de trabajo ágil, seguro y optimizado, que contribuye a la mejora de la calidad del servicio médico brindado a los pacientes.

## Objetivo de entrega 3

En esta entrega se implemento los controladores REST principales con seguridad basada en JWT, usando DTOs y MapStruct, junto con pruebas automatizadas en Postman, asegurando una arquitectura limpia, segura y lista para despliegue en servidor.

## Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalados los siguientes programas en tu sistema:

- **Java 17 o superior** (para ejecutar el proyecto Spring Boot)
- **Maven 3.8 o superior** (para la construcción del proyecto)
- **PostgreSQL** (como base de datos)
- **IDE (opcional)**: Intellij IDEA, Eclipse, VSCode, o cualquier otro editor compatible con Java y Spring Boot.

## Instalación

1. **Clonar el repositorio**:

   Clona este proyecto en tu máquina local utilizando Git:

   ```bash
   https://github.com/Computacion-2-2025/proyecto-del-curso-ccr.git

2. **Ejecución de pruebas**:

   Para ejecutar las pruebas y verificar el correcto funcionamiento de la aplicación, ejecuta el siguiente comando:

    ```bash
    mvn clean test  

3. **Ejecución del proyecto**:

   Para ejecutar el proyecto, usa el siguiente comando:

   ```bash
    mvn spring-boot:run

4. **Despliegue de la aplicación**:

   Para acceder a la aplicacion desplegada, coloca el siguiente link en tu navegador de preferencia:

   ```bash
    http://10.147.19.21:8080/g1ccr/login

5. **Uso de la aplicación**:

   Los datos predefinidos en la entrada del apartado de login, "admin" y "123456" es un usuario predefinido para acceder rapidamente y comprobar la parte administrativa de la aplicacion, hay que tener en cuenta que como parte de la entrega solo esta implementada la parte del menu -> "Admin Usuarios" donde esta la parte de gestion de usuarios. El usuario puede registrarse como usuario normal en el link registrate del apartado de login si no tiene una cuenta creada y llenar el formulario respectivo, al crear la cuenta puedes ingresar tu "usuario" y "password" en el apartado de login para iniciar sesion en tu cuenta.

      
6. **resultados de Jacoco**   
Se tiene una cobertura del 100% en cuanto a los servicios solicitados
![alt text](<jacoco.png>)

7. **Videos de prueba del proyecto**:

   Aqui estaran todos los videos de prueba del proyecto, actualizado por cada entrega:

   Link: https://drive.google.com/drive/folders/1Ht_G1X5HLWBC3ynD3_I4aguyx-lPqKzC?usp=drive_link


