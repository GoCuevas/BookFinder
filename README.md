# 📚 BookFinder

BookFinder es una aplicación desarrollada en **Java 17 + Spring Boot** que permite buscar libros en la API pública de [Gutendex](https://gutendex.com/) y almacenarlos en una base de datos **PostgreSQL**.

## 🚀 Características

1. **Buscar libros por título** (API Gutendex) y registrar el mejor resultado (mayor número de descargas) en la base de datos.
2. **Listar libros registrados** en la base de datos.
3. **Listar autores registrados**.
4. **Listar autores vivos** en un año específico.
5. **Listar libros por idioma**.

---

## 📦 Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Gutendex API**

---

## ⚙️ Requisitos previos

- **Java 17 o superior** instalado ([Descargar Java](https://adoptium.net/))
- **Maven** instalado ([Descargar Maven](https://maven.apache.org/download.cgi))
- **PostgreSQL** instalado y en ejecución ([Descargar PostgreSQL](https://www.postgresql.org/download/))
- **IntelliJ IDEA** o tu IDE de preferencia

---

## 🗄 Configuración de la base de datos

1. Crear la base de datos:
```sql
CREATE DATABASE booksdb;
```

2. Configurar las credenciales en el archivo `application.yml` o mediante **variables de entorno**:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/booksdb
    username: postgres
    password: ${LTR_PASSWORD}  # Variable de entorno con la contraseña
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

⚠ **Recomendado:** Usar variables de entorno para no exponer credenciales.

---

## ▶ Ejecución del proyecto

1. Clonar el repositorio:
```bash
git clone https://github.com/GoCuevas/BookFinder.git
cd BookFinder
```

2. Compilar y ejecutar con Maven:
```bash
mvn spring-boot:run
```

3. El menú en consola mostrará las opciones disponibles:
```
1 - Buscar libro por título
2 - Listar libros registrados
3 - Listar autores registrados
4 - Listar autores vivos en un año
5 - Listar libros por idioma
0 - Salir
```

---

## 📡 Ejemplo de búsqueda

Entrada:
```
1
Título a buscar: Pride
```

Salida:
```
------ LIBRO -----
Titulo: Pride and Prejudice
Autor: Austen, Jane
Idioma: en
Numero de descargas: 76493
-------------------
```

El libro se guardará automáticamente en la base de datos.

---

## 📌 Notas

- El proyecto está limpio, sin archivos temporales (`.idea/`, `target/`) gracias al `.gitignore`.
- Se usan **Lombok annotations** para simplificar el código.

---

## 👨‍💻 Autor

**Gonzalo Cuevas**  
[LinkedIn](https://www.linkedin.com/in/gonzalocuevas-maritimeagent/) | [GitHub](https://github.com/GoCuevas)

