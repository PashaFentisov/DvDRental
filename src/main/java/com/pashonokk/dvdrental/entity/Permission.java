package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    @Setter(AccessLevel.PRIVATE)
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
        role.getPermissions().add(this);
    }

    public void addRoles(List<Role> roles) {
        for (Role role : roles) {
            addRole(role);
        }
    }

    public void removeRoles(Set<Role> roles) {
        for(Role role: roles){
            role.removePermission(this);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return name.equals(that.name);
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
