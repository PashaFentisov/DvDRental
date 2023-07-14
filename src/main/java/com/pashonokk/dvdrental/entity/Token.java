package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Token {
    @Id
    private Long userId;
    private String value = UUID.randomUUID().toString();
    private LocalDateTime createTime;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public void addUser(User user) {
        this.user = user;
        user.setToken(this);
        createTime = LocalDateTime.now();
    }
}
