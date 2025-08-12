# Libros - Spring Boot + Gutendex + JPA (PostgreSQL)

## Requisitos
- Java 21 (o 17+)
- Maven 3.9+
- PostgreSQL (base `booksdb` creada)

## Configuración
Edita `src/main/resources/application.yml` con tus credenciales/postgres.

## Ejecutar
```bash
mvn spring-boot:run
```

## Endpoints
1) Buscar por título (API externa + persistir solo nuevos):  
   `POST http://localhost:8080/api/external/search?title=don%20quixote`
2) Libros (BDD):  
   `GET  http://localhost:8080/api/books`
3) Autores (BDD):  
   `GET  http://localhost:8080/api/authors`
4) Autores vivos en un año (BDD):  
   `GET  http://localhost:8080/api/authors/alive?year=1890`
5) Libros por idioma (BDD):  
   `GET  http://localhost:8080/api/books/by-language?code=es`
