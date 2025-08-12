package com.aluracursos.desafio.service;

import com.aluracursos.desafio.client.GutendexClient;
import com.aluracursos.desafio.domain.*;
import com.aluracursos.desafio.model.*;
import com.aluracursos.desafio.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;
  private final GutendexClient gutendex;

  public BookService(BookRepository bookRepository,
                     AuthorRepository authorRepository,
                     GutendexClient gutendex) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
    this.gutendex = gutendex;
  }

  @Transactional
  public List<Book> searchAndPersistByTitle(String title) {
    Datos datos = gutendex.search(title);
    if (datos == null || datos.resultados() == null) return List.of();

    List<Book> persisted = new ArrayList<>();
    for (DatosLibros dl : datos.resultados()) {
      Integer gId = dl.id();
      if (gId == null || bookRepository.findByGutenbergId(gId).isPresent()) continue;
      Book book = mapToBook(dl);
      persisted.add(bookRepository.save(book));
    }
    return persisted;
  }

  public List<Book> listAllBooks() { return bookRepository.findAll(); }

  public List<Author> listAllAuthors() { return authorRepository.findAll(); }

  public List<Author> authorsAliveIn(int year) { return authorRepository.findAliveInYear(year); }

  public List<Book> booksByLanguage(String code) { return bookRepository.findByLanguage(code); }

  // ====== “Mejor resultado” (ya lo tenías) ======
  public static record BestSearchResult(Book book, boolean savedNew) {}

  @Transactional
  public BestSearchResult searchBestAndPersistByTitle(String title) {
    Datos datos = gutendex.search(title);
    if (datos == null || datos.resultados() == null || datos.resultados().isEmpty())
      return new BestSearchResult(null, false);

    Optional<DatosLibros> opt = datos.resultados().stream()
            .filter(dl -> dl.titulo() != null && dl.titulo().toLowerCase().contains(title.toLowerCase()))
            .max(Comparator.comparingInt(dl -> dl.numeroDeDescargas() == null ? 0 : dl.numeroDeDescargas().intValue()));

    if (opt.isEmpty()) return new BestSearchResult(null, false);
    DatosLibros dl = opt.get();

    Integer gId = dl.id();
    if (gId == null) return new BestSearchResult(null, false);

    Book book = bookRepository.findByGutenbergId(gId).orElse(null);
    boolean saved = false;
    if (book == null) {
      book = bookRepository.save(mapToBook(dl));
      saved = true;
    }
    return new BestSearchResult(book, saved);
  }

  // ====== Helpers de mapeo ======
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
