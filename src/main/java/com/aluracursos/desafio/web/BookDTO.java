package com.aluracursos.desafio.web;

import com.aluracursos.desafio.domain.Book;
import java.util.Set;
import java.util.stream.Collectors;

public record BookDTO(
    Long id,
    Integer gutenbergId,
    String title,
    Integer downloadCount,
    Set<String> languages,
    Set<String> authors
) {
  public static BookDTO from(Book b) {
    return new BookDTO(
        b.getId(),
        b.getGutenbergId(),
        b.getTitle(),
        b.getDownloadCount(),
        b.getLanguages(),
        b.getAuthors().stream().map(a -> a.getName()).collect(Collectors.toSet())
    );
  }
}
