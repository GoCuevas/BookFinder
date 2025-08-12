package com.aluracursos.desafio.web;

import com.aluracursos.desafio.domain.Author;

public record AuthorDTO(Long id, String name, Integer birthYear, Integer deathYear) {
  public static AuthorDTO from(Author a) {
    return new AuthorDTO(a.getId(), a.getName(), a.getBirthYear(), a.getDeathYear());
  }
}
