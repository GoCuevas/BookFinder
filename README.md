# 📚 BookFinder

Aplicación Java Spring Boot que permite buscar libros en la API pública de **Gutendex**, mostrando el libro más popular según número de descargas, y guardándolo en una base de datos **PostgreSQL** para futuras consultas.

---

## 🚀 Requisitos Previos

Asegúrate de tener instalados:

- **Java**: versión 17 o superior  
  [Descargar Java 17](https://adoptium.net/temurin/releases/?version=17)

- **Maven**: versión 3.9.x o superior  
  [Instalar Maven](https://maven.apache.org/install.html)

- **PostgreSQL**: versión 14 o superior  
  [Descargar PostgreSQL](https://www.postgresql.org/download/)

- **Git** (para clonar el repositorio)  
  [Instalar Git](https://git-scm.com/downloads)

---

## 🛠 Configuración de la Base de Datos

1. Inicia sesión en PostgreSQL y crea la base de datos:
   ```sql
   CREATE DATABASE booksdb;
   ```

2. Configura un usuario y contraseña (por ejemplo, `postgres` con tu clave de acceso).

3. Crea una **variable de entorno** para la contraseña:
   - **Windows (PowerShell)**:
     ```powershell
     setx LTR_PASSWORD "TuContraseñaPostgres"
     ```
   - **Linux / MacOS**:
     ```bash
     export LTR_PASSWORD="TuContraseñaPostgres"
     ```

---

## 📦 Instalación del Proyecto

1. Clona el repositorio:
   ```bash
   git clone https://github.com/GoCuevas/BookFinder.git
   cd BookFinder
   ```

2. Verifica que el archivo `application.yml` esté configurado así:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://127.0.0.1:5432/booksdb
       username: postgres
       password: ${LTR_PASSWORD}
       driver-class-name: org.postgresql.Driver
     jpa:
       database-platform: org.hibernate.dialect.PostgreSQLDialect
       hibernate:
         ddl-auto: update
       show-sql: true
       properties:
         hibernate.format_sql: true
   server:
     port: 8080
   ```

---

## ▶️ Ejecución

En la carpeta del proyecto, ejecuta:

```bash
mvn spring-boot:run
```

La aplicación iniciará en **consola interactiva** para realizar búsquedas y almacenar resultados.

---

## 📋 Uso

- **Opción 1**: Buscar un libro por título (muestra y guarda el más popular).
- **Opción 2+**: Consultas adicionales según el menú.
- **Opción 0**: Salir de la aplicación.

---

## 🧹 Limpieza de Datos

Para reiniciar la base de datos y comenzar desde cero:

```sql
TRUNCATE TABLE book_authors, books, authors RESTART IDENTITY CASCADE;
```

---

## 📄 Licencia

Este proyecto es de uso educativo. Puedes modificarlo y adaptarlo libremente.
