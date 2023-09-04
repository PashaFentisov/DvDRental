package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "rentals")
@Audited
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private Film film;
    @OneToMany(mappedBy = "inventory")
    private List<Rental> rentals = new ArrayList<>();
    private OffsetDateTime lastUpdate;
    private Boolean isDeleted;
    private Boolean isAvailable;

    public Inventory(OffsetDateTime lastUpdate, Boolean isDeleted, Boolean isAvailable) {
        this.lastUpdate = lastUpdate;
        this.isDeleted = isDeleted;
        this.isAvailable = isAvailable;
    }

    public void addFilm(Film film) {
        this.film = film;
        film.getInventories().add(this);
    }

    public void addStore(Store store) {
        this.store = store;
        store.getInventories().add(this);
    }
}
