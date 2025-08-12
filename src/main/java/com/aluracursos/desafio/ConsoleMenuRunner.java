package com.aluracursos.desafio;

import com.aluracursos.desafio.domain.Author;
import com.aluracursos.desafio.domain.Book;
import com.aluracursos.desafio.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;


import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ConsoleMenuRunner implements CommandLineRunner {

    private final BookService service;
    private final Scanner in = new Scanner(System.in); // ¡no cerrar!
    private final ApplicationContext ctx;

    public ConsoleMenuRunner(BookService service, ApplicationContext ctx) {
        this.service = service;
        this.ctx = ctx;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=== 📚 Menú - Biblioteca (Gutendex + JPA) ===");
        String opt;
        do {
            printMenu();
            opt = in.nextLine().trim();
            try {
                switch (opt) {
                    case "1" -> buscarYGuardar();
                    case "2" -> listarLibros();
                    case "3" -> listarAutores();
                    case "4" -> autoresVivosEnAnio();
                    case "5" -> librosPorIdioma();
                    case "0" -> {
                        System.out.println("👋 Cerrando aplicación...");
                        int code = SpringApplication.exit(ctx, () -> 0);
                        System.exit(code);
                    }

                    default -> System.out.println("Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println("Error en la opción: " + e.getMessage());
            }
        } while (!"0".equals(opt));
    }

    private void printMenu() {
        System.out.println("""
        \nElige una opción:
        1) Buscar libros por título
        2) Listar libros buscados
        3) Listar autores 
        4) Listar autores vivos en un año
        5) Listar libros por idioma
        0) Salir
        > """);
    }

    private void buscarYGuardar() {
            System.out.print("Título a buscar: ");
            String title = in.nextLine().trim();

            var res = service.searchBestAndPersistByTitle(title);
            if (res.book() == null) {
                System.out.println("No se encontraron coincidencias en el título.");
                return;
            }

            printBestFormat(res.book());
            System.out.println(res.savedNew() ? "Se registró en la BD." : "Ya existía en la BD.");

    }


    private void listarLibros() {
        var libros = service.listAllBooks();
        if (libros.isEmpty()) { System.out.println("No hay libros registrados."); return; }
        libros.stream().limit(30).forEach(this::printBook);
        if (libros.size() > 30) System.out.printf("... (%d en total)%n", libros.size());
    }

    private void listarAutores() {
        var autores = service.listAllAuthors();
        if (autores.isEmpty()) { System.out.println("No hay autores registrados."); return; }
        autores.stream().limit(50).forEach(a ->
                System.out.printf("- [%d] %s (%s–%s)%n",
                        a.getId(), a.getName(),
                        a.getBirthYear() == null ? "?" : a.getBirthYear(),
                        a.getDeathYear() == null ? "?" : a.getDeathYear()));
        if (autores.size() > 50) System.out.printf("... (%d en total)%n", autores.size());
    }

    private void autoresVivosEnAnio() {
        System.out.print("Año (YYYY): ");
        int year = parseIntSafe(in.nextLine());
        var autores = service.authorsAliveIn(year);
        if (autores.isEmpty()) { System.out.println("No hay autores vivos ese año."); return; }
        autores.forEach(a ->
                System.out.printf("- %s (%s–%s)%n",
                        a.getName(),
                        a.getBirthYear() == null ? "?" : a.getBirthYear(),
                        a.getDeathYear() == null ? "?" : a.getDeathYear()));
    }

    private void librosPorIdioma() {
        System.out.print("Código de idioma (ej: es, en, pt, fr): ");
        String code = in.nextLine().trim();
        var libros = service.booksByLanguage(code);
        if (libros.isEmpty()) { System.out.println("No hay libros para ese idioma."); return; }
        libros.forEach(this::printBook);
    }

    private void printBook(Book b) {
        String langs = String.join(",", b.getLanguages());
        String auths = b.getAuthors().stream().map(Author::getName).collect(Collectors.joining("; "));
        System.out.printf("- #%d GID=%d | %s | langs=%s | autores=%s | downloads=%s%n",
                b.getId(), b.getGutenbergId(), b.getTitle(), langs, auths,
                b.getDownloadCount() == null ? "?" : b.getDownloadCount());
    }

    private void printBestFormat(Book b) {
        String autor = b.getAuthors().isEmpty()
                ? "?"
                : b.getAuthors().stream().map(Author::getName).collect(java.util.stream.Collectors.joining(", "));
        String idioma = b.getLanguages().stream().findFirst().map(this::cap).orElse("?");
        String downloads = b.getDownloadCount() == null ? "?" : String.valueOf(b.getDownloadCount());

        System.out.println("\n------ LIBRO -----\n");
        System.out.println("Titulo: " + b.getTitle());
        System.out.println("Autor: " + autor);
        System.out.println("Idioma: " + idioma);
        System.out.println("Numero de descargas: " + downloads);
        System.out.println("\n-------------------\n");
    }

    private String cap(String code) {
        if (code == null || code.isBlank()) return "?";
        return code.substring(0, 1).toUpperCase() + code.substring(1).toLowerCase();
    }


    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }
}
