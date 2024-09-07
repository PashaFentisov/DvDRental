package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
@AllArgsConstructor
@Builder
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private String number;
    private boolean isMain = true;
    private boolean isDeleted;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    public void addAddress(Address address) {
        this.address = address;
        address.getPhones().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return number.equals(phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
