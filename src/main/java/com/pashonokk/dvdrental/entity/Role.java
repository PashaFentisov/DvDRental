package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "role", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Setter(AccessLevel.PRIVATE)
    private Set<User> users = new HashSet<>();
    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Setter(AccessLevel.PRIVATE)
    private Set<Authority> authorities = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
        user.setRole(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.setRole(null);
    }
}
