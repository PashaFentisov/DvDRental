package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column(name = "is_verified")
    private boolean isVerified = false;
    @ManyToMany(mappedBy = "users", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Role> roles;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Token token;

}
