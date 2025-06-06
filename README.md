# 🧠 Neuroprevia

Sistema de evaluación previa para diagnóstico temprano de autismo. Desarrollado con **Spring Boot** y **PostgreSQL**, incluye gestión de usuarios (pacientes, doctores, administradores), exámenes interactivos, resultados PDF, estadísticas y más.

---

##  💻  Tecnologías

- Java 17
- Spring Boot
- Spring Security
- Prometheus
- Thymeleaf
- PostgreSQL
- Maven
- Bootstrap 5
- Actuator
- Micrometer
- Lombook

---

## 👥 Roles y funcionalidades

| Rol      | Funcionalidades principales                                                                |
| -------- |--------------------------------------------------------------------------------------------|
| Paciente | Ver perfil, realizar exámenes, descargar resultados                                        |
| Doctor   | Crear exámenes, ver pacientes, ver resultados, agregar la interpretación de los resultados |
| Admin    | Gestionar usuarios, visualizar estadísticas.                                               |


---

## ⚙️ Configuración del Proyecto

### 1. Clonar el repositorio
```bash
git clone https://github.com/F7MH2R/AustismoSystem.git
cd neuroprevia
```
### 2. Es necesario tener instalado el servidor PostgreSQL
Lo puedes descargar desde el sitio oficial: https://www.postgresql.org/download/

### 3. Se debe modificar el archivo application.properties
Los parametros de la base de datos se encuentran en el archivo: application.properties, los valores a modificar son los siguientes:
<pre>
spring.datasource.url=jdbc:postgresql://localhost:5432/neuroprevia_db
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
</pre>
Éstos valores según la configuración local de tu servidor PostgreSQL.

### 4. Ejecutar el proyecto
Luego de actualizar los parametros mencionados en el numeral anterior, ejecuta este comando:

```bash
./mvnw spring-boot:run
```

---
📩 Contacto
Desarrollado por el equipo de Neuroprevia
<br>📍 El Salvador