package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private String firstName;
    @Column(unique = true, nullable = false, updatable = false)
    private String lastName;
    private String biography;
    @Column(unique = true, nullable = false, updatable = false)
    private OffsetDateTime birthDate;
    private OffsetDateTime lastUpdate;
    @ManyToMany(mappedBy = "actors", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Setter(AccessLevel.PRIVATE)
    private Set<Film> films = new HashSet<>();
    private Boolean isDeleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return firstName.equals(actor.firstName) && lastName.equals(actor.lastName) && birthDate.equals(actor.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthDate);
    }
}
