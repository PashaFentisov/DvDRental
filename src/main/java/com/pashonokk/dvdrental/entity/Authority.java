package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(name = "role_authorities", joinColumns = @JoinColumn(name = "authority_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();
}
