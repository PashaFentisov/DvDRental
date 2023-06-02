package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Boolean isVerified = false;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Token token;

}
