package com.aluracursos.desafio.web;

import com.aluracursos.desafio.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

  private final BookService service;

  @PostMapping("/external/search")
  public List<BookDTO> searchAndPersist(@RequestParam String title) {
    return service.searchAndPersistByTitle(title).stream().map(BookDTO::from).toList();
  }

  @GetMapping("/books")
  public List<BookDTO> listBooks() {
    return service.listAllBooks().stream().map(BookDTO::from).toList();
  }

  @GetMapping("/authors")
  public List<AuthorDTO> listAuthors() {
    return service.listAllAuthors().stream().map(AuthorDTO::from).toList();
  }

  @GetMapping("/authors/alive")
  public List<AuthorDTO> authorsAlive(@RequestParam int year) {
    return service.authorsAliveIn(year).stream().map(AuthorDTO::from).toList();
  }

  @GetMapping("/books/by-language")
  public List<BookDTO> booksByLanguage(@RequestParam String code) {
    return service.booksByLanguage(code).stream().map(BookDTO::from).toList();
  }

  @PostMapping("/books/search-best")
  public BestSearchResponse searchBest(@RequestParam String title) {
    var res = service.searchBestAndPersistByTitle(title);
    if (res.book() == null) {
      // 404 si no hubo coincidencias
      throw new org.springframework.web.server.ResponseStatusException(
              org.springframework.http.HttpStatus.NOT_FOUND, "No se encontraron coincidencias");
    }
    return new BestSearchResponse(BookDTO.from(res.book()), res.savedNew());
  }


}
