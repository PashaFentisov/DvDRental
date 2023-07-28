package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    @Column(unique = true, nullable = false, updatable = false)
    private String title;
    @Column(unique = true, nullable = false, updatable = false)
    private String description;
    private LocalDate releaseYear;
    private Duration rentalDuration;
    private Double rentalRate;
    private Duration length;
    private Double replacementCost;
    private Double rating;
    private LocalDate lastUpdate;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "film_language",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private Set<Language> languages = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "film_category",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private Set<Actor> actors = new HashSet<>();


    public void addCategory(Category category) {
        categories.add(category);
        category.getFilms().add(this);
    }

    public void addCategory(List<Category> categories) {
        for (Category category : categories) {
            addCategory(category);
        }
    }
    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getFilms().remove(this);
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

    public void removeLanguage(Language language) {
        language.getFilms().remove(this);
        this.languages.remove(language);
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

    public void removeActor(Actor actor) {
        actor.getFilms().remove(this);
        this.actors.remove(actor);
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
