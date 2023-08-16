package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private String name;
    @OneToMany(mappedBy = "role", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Setter(AccessLevel.PRIVATE)
    private Set<User> users = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
        user.setRole(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
