package com.pashenko.Board.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reg_tokens")
public class ConfirmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @Column(name = "token")
    private String uuid;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    public ConfirmToken(User user, String uuid, LocalDateTime expirationDate) {
        this.user = user;
        this.uuid = uuid;
        this.expirationDate = expirationDate;
    }

    public ConfirmToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
