package com.aluracursos.desafio.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors",
       uniqueConstraints = @UniqueConstraint(columnNames = {"name","birth_year","death_year"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Author {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "birth_year")
  private Integer birthYear;

  @Column(name = "death_year")
  private Integer deathYear;

  @ManyToMany(mappedBy = "authors")
  private Set<Book> books = new HashSet<>();
}
