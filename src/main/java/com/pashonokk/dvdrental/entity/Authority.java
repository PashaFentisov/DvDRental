package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(name = "role_authorities", joinColumns = @JoinColumn(name = "authority_id"),  //todo потенційна проблема з сетом
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Setter(AccessLevel.PRIVATE)
    private Set<Role> roles = new HashSet<>();
}
