package com.pashonokk.dvdrental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(unique = true, nullable = false, updatable = false)
    private String description;
    private OffsetDateTime releaseYear;
    private Double rentalRate;
    @NotAudited
    private Duration length;
    private Double replacementCost;
    private Double rating;
    private OffsetDateTime lastUpdate;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "film_language",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    @Setter(AccessLevel.PRIVATE)
    private Set<Language> languages = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "film_category",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Setter(AccessLevel.PRIVATE)
    private Set<Category> categories = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    @Setter(AccessLevel.PRIVATE)
    private Set<Actor> actors = new HashSet<>();

    @OneToMany(mappedBy = "film", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @Setter(AccessLevel.PRIVATE)
    private List<Inventory> inventories = new ArrayList<>();

    private Boolean isDeleted;


    public void addCategory(Category category) {
        categories.add(category);
        category.getFilms().add(this);
    }

    public void addCategory(List<Category> categories) {
        for (Category category : categories) {
            addCategory(category);
        }
    }

    public void addLanguage(Language language) {
        language.getFilms().add(this);
        this.languages.add(language);
    }

    public void addLanguage(List<Language> languages) {
        for (Language language : languages) {
            addLanguage(language);
        }
    }

    public void addActor(Actor actor) {
        actor.getFilms().add(this);
        this.actors.add(actor);
    }

    public void addActor(List<Actor> actors) {
        for (Actor actor : actors) {
            addActor(actor);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return description.equals(film.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
