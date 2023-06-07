package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@ToString(exclude = "user")
public class Token {
    @Id
    private Long userId;
    private String value = UUID.randomUUID().toString();
    private LocalTime createTime;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public void addUser(User user) {
        this.user = user;
        user.setToken(this);
        createTime = LocalTime.now();
    }
}
