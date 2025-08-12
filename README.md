# BookFinder - Spring Boot + Gutendex + JPA (PostgreSQL)

Aplicación en Java + Spring Boot que permite buscar libros en la API pública **Gutendex** y registrar los resultados en una base de datos **PostgreSQL**, aplicando persistencia con **Spring Data JPA**.

## Requisitos
- **Java 21** (o 17+)
- **Maven 3.9+**
- **PostgreSQL** con la base `booksdb` creada

## Configuración

El proyecto está configurado para leer la contraseña de Postgres desde la variable de entorno `LTR_PASSWORD`.

Ejemplo para definirla:

**Windows (PowerShell)**
```powershell
setx LTR_PASSWORD "tu_clave_postgres"
```

**Linux / Mac**
```bash
export LTR_PASSWORD=tu_clave_postgres
```

Si quieres también puedes parametrizar el usuario y la URL modificando en `application.yml`:
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://127.0.0.1:5432/booksdb}
    username: ${DB_USER:postgres}
    password: ${LTR_PASSWORD}
```

## Ejecutar en consola
```bash
mvn spring-boot:run
```

En el menú de consola podrás:
1. Buscar libro por título (API externa + persistir solo si no existe en la BD)
2. Listar libros registrados
3. Listar autores registrados
4. Listar autores vivos en un año específico
5. Listar libros por idioma
0. Salir

## Formato de salida en búsquedas
```
------ LIBRO -----
Título: Pride and Prejudice
Autor: Austen, Jane
Idioma: en
Número de descargas: 76493
-------------------
```

## Endpoints REST
1. **Buscar por título** (API externa + persistir solo nuevos)  
   `POST http://localhost:8080/api/external/search?title=don%20quixote`
2. **Listar libros** (BDD)  
   `GET  http://localhost:8080/api/books`
3. **Listar autores** (BDD)  
   `GET  http://localhost:8080/api/authors`
4. **Autores vivos en un año** (BDD)  
   `GET  http://localhost:8080/api/authors/alive?year=1890`
5. **Libros por idioma** (BDD)  
   `GET  http://localhost:8080/api/books/by-language?code=es`
