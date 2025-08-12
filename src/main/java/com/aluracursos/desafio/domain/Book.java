package com.aluracursos.desafio.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "gutenberg_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Book {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "gutenberg_id", nullable = false)
  private Integer gutenbergId;

  @Column(nullable = false)
  private String title;

  @Column(name = "download_count")
  private Integer downloadCount;

  @ElementCollection
  @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
  @Column(name = "language", length = 5)
  private Set<String> languages = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "book_authors",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "author_id"))
  private Set<Author> authors = new HashSet<>();
}
