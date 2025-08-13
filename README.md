# 📚 BookFinder

Aplicación Java con Spring Boot que permite buscar libros usando la API pública de [Gutendex](https://gutendex.com/) y registrar los resultados en una base de datos PostgreSQL.

## 🚀 Características

- **Buscar libros por título** usando la API de Gutendex.
- Registrar automáticamente el libro más popular encontrado (por número de descargas) en la base de datos.
- Evitar duplicados: si el libro ya existe, no se vuelve a registrar.
- Consultar todos los libros registrados.
- Listar autores registrados.
- Listar autores vivos en un año determinado.
- Listar libros por idioma.

## 🛠 Tecnologías utilizadas

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Gutendex API**

## 📂 Estructura del proyecto

```
src/main/java/com/aluracursos/desafio/
│
├── domain/        # Entidades JPA (Book, Author)
├── model/         # Clases DTO y utilidades de consumo API
├── repository/    # Repositorios Spring Data JPA
├── service/       # Lógica de negocio (BookService)
└── web/           # Controladores REST
```

## ⚙️ Configuración

1. Clona este repositorio:
   ```bash
   git clone https://github.com/GoCuevas/BookFinder.git
   cd BookFinder
   ```

2. Crea una base de datos en PostgreSQL:
   ```sql
   CREATE DATABASE booksdb;
   ```

3. Configura las credenciales en el archivo `application.yml` usando variables de entorno:
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

4. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```

## 📋 Menú de consola

```
------ MENÚ ------
1. Buscar libro por título (solo guarda el más popular)
2. Listar todos los libros registrados
3. Listar todos los autores
4. Listar autores vivos en un año determinado
5. Listar libros por idioma
0. Salir
------------------
```

## 📜 Ejemplo de salida

```
------ LIBRO ------
Titulo: Pride and Prejudice
Autor: Austen, Jane
Idioma: en
Numero de descargas: 76493
-------------------
```

## 🌐 Endpoints REST

- `GET /api/books` → Lista todos los libros registrados
- `GET /api/authors` → Lista todos los autores
- `GET /api/authors/alive/{year}` → Lista autores vivos en un año dado
- `GET /api/books/lang/{code}` → Lista libros por código de idioma

## 👨‍💻 Autor

**Gonzalo Cuevas**  
Proyecto ficticio para práctica de **Java Avanzado + Spring Boot** (Oracle Next Education - Alura Latam)
