package com.dompoo.onlineshopping.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne
    private Users users;

    @Builder
    public Session(Users users) {
        this.accessToken = UUID.randomUUID().toString();
        this.users = users;
    }
}
