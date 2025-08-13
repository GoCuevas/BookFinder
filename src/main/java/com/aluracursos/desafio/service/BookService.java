package com.aluracursos.desafio.service;

import com.aluracursos.desafio.domain.Author;
import com.aluracursos.desafio.domain.Book;
import com.aluracursos.desafio.model.ConsumoAPI;
import com.aluracursos.desafio.model.ConvierteDatos;
import com.aluracursos.desafio.model.Datos;
import com.aluracursos.desafio.model.DatosAutor;
import com.aluracursos.desafio.model.DatosLibros;
import com.aluracursos.desafio.repository.AuthorRepository;
import com.aluracursos.desafio.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;

  private final ConsumoAPI consumoAPI = new ConsumoAPI();
  private final ConvierteDatos convierteDatos = new ConvierteDatos();

  private static final String BASE = "https://gutendex.com/books/";

  public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
  }

  /* ------------------- BÚSQUEDA GENERAL (PERSISTS TODOS LOS NUEVOS) ------------------- */
  @Transactional
  public List<Book> searchAndPersistByTitle(String title) {
    String q = URLEncoder.encode(title, StandardCharsets.UTF_8);
    String url = BASE + "?search=" + q + "&sort=popular";

    String json = consumoAPI.obtenerDatos(url);
    Datos datos = convierteDatos.obtenerDatos(json, Datos.class);
    if (datos == null || datos.resultados() == null) return List.of();

    List<Book> persisted = new ArrayList<>();
    for (DatosLibros dl : datos.resultados()) {
      Integer gId = dl.id();
      if (gId == null) continue;
      if (bookRepository.findByGutenbergId(gId).isPresent()) continue;

      Book book = mapToBook(dl);
      persisted.add(bookRepository.save(book));
    }
    return persisted;
  }

  /* ------------------- BEST RESULT (SOLO GUARDA EL MEJOR) ------------------- */
  public static record BestSearchResult(Book book, boolean savedNew) {}

  @Transactional
  public BestSearchResult searchBestAndPersistByTitle(String title) {
    String q = URLEncoder.encode(title, StandardCharsets.UTF_8);
    String url = BASE + "?search=" + q + "&sort=popular";

    String json = consumoAPI.obtenerDatos(url);
    Datos datos = convierteDatos.obtenerDatos(json, Datos.class);
    if (datos == null || datos.resultados() == null || datos.resultados().isEmpty()) {
      return new BestSearchResult(null, false);
    }

    // Filtra por coincidencia en TÍTULO y toma el de mayor download_count
    Optional<DatosLibros> opt = datos.resultados().stream()
            .filter((DatosLibros dl) -> dl.titulo() != null &&
                    dl.titulo().toLowerCase().contains(title.toLowerCase()))
            .max(Comparator.comparingInt(dl -> dl.numeroDeDescargas() == null ? 0 : dl.numeroDeDescargas().intValue()));

    if (opt.isEmpty()) return new BestSearchResult(null, false);
    DatosLibros dl = opt.get();

    Integer gId = dl.id();
    if (gId == null) return new BestSearchResult(null, false);

    boolean saved = false;
    Book book = bookRepository.findByGutenbergId(gId).orElse(null);
    if (book == null) {
      book = bookRepository.save(mapToBook(dl));
      saved = true;
    }

    // ⚠️ Evita LazyInitialization: devuelve SIEMPRE la versión con colecciones cargadas
    Book loaded = bookRepository.findWithDetailsByGutenbergId(gId).orElse(book);
    return new BestSearchResult(loaded, saved);
  }

  /* ------------------- LISTADOS (REPO YA TRAE COLECCIONES CON ENTITYGRAPH) ------------------- */
  public List<Book> listAllBooks() { return bookRepository.findAll(); }

  public List<Author> listAllAuthors() { return authorRepository.findAll(); }

  public List<Author> authorsAliveIn(int year) { return authorRepository.findAliveInYear(year); }

  public List<Book> booksByLanguage(String code) { return bookRepository.findByLanguage(code); }

  /* ------------------- HELPERS DE MAPE0 ------------------- */
  private Book mapToBook(DatosLibros dl) {
    Set<Author> authors = findOrCreateAuthors(dl.autor());
    return Book.builder()
            .gutenbergId(dl.id())
            .title(dl.titulo())
            .downloadCount(dl.numeroDeDescargas() == null ? null : dl.numeroDeDescargas().intValue())
            .languages(new HashSet<>(Optional.ofNullable(dl.idiomas()).orElse(List.of())))
            .authors(authors)
            .build();
  }

  private Set<Author> findOrCreateAuthors(List<DatosAutor> lista) {
    if (lista == null || lista.isEmpty()) return Set.of();
    return lista.stream().map(a ->
            authorRepository.findByNameAndBirthYearAndDeathYear(
                    a.nombre(), a.fechaDeNacimiento(), a.fechaDeFallecimiento()
            ).orElseGet(() -> authorRepository.save(Author.builder()
                    .name(a.nombre())
                    .birthYear(a.fechaDeNacimiento())
                    .deathYear(a.fechaDeFallecimiento())
                    .build()))
    ).collect(Collectors.toSet());
  }
}
