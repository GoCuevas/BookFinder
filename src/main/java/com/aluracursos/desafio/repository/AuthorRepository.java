package com.aluracursos.desafio.repository;

import com.aluracursos.desafio.domain.Author;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface AuthorRepository extends JpaRepository<Author, Long> {
  Optional<Author> findByNameAndBirthYearAndDeathYear(String name, Integer birthYear, Integer deathYear);

  @Query("""
         select a from Author a
         where (a.birthYear is not null and a.birthYear <= :year)
           and (a.deathYear is null or a.deathYear >= :year)
         """)
  List<Author> findAliveInYear(@Param("year") int year);
}
