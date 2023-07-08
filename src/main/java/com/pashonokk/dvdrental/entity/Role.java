package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@ToString(exclude = "users")
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
    private Set<Authority> authorities = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
        user.setRole(this);
    }
}
