package com.aluracursos.desafio.repository;

import com.aluracursos.desafio.domain.Book;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.*;

public interface BookRepository extends JpaRepository<Book, Long> {

  Optional<Book> findByGutenbergId(Integer gutenbergId);

  // Carga autores + idiomas para evitar LazyInitializationException
  @EntityGraph(attributePaths = {"languages", "authors"})
  @Query("select b from Book b where b.gutenbergId = :gid")
  Optional<Book> findWithDetailsByGutenbergId(@Param("gid") Integer gutenbergId);

  // Sobrescribimos findAll con EntityGraph
  @Override
  @EntityGraph(attributePaths = {"languages", "authors"})
  List<Book> findAll();

  // Consulta por idioma con colecciones cargadas
  @EntityGraph(attributePaths = {"languages", "authors"})
  @Query("select b from Book b join b.languages l where l = :code")
  List<Book> findByLanguage(@Param("code") String code);
}
