package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private String title;
    @Column(unique = true, nullable = false, updatable = false)
    private String description;
    private LocalDate releaseYear;
//    @Column(columnDefinition = "interval")
    private Duration rentalDuration;
    private Double rentalRate;
//    @Column(columnDefinition = "interval")
    private Duration length;
    private Double replacementCost;
    private Double rating;
    private LocalDate lastUpdate;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "film_category",
               joinColumns = @JoinColumn(name = "film_id"),
               inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Setter(AccessLevel.PRIVATE)
    private Set<Category> categories = new HashSet<>();


    public void addCategory(Category category) {
        categories.add(category);
        category.getFilms().add(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getFilms().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return title.equals(film.title) && description.equals(film.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }
}
