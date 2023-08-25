package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Audited
@ToString(exclude = "user")
public class Token {
    @Id
    private Long userId;
    private String value = UUID.randomUUID().toString();
    private OffsetDateTime createTime;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public void addUser(User user) {
        this.user = user;
        user.setToken(this);
        createTime = OffsetDateTime.now();
    }
}
