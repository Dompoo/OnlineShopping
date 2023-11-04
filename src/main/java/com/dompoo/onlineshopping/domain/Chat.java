package com.dompoo.onlineshopping.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Builder
    public Chat(String message, Conversation conversation) {
        this.message = message;
        this.createdAt = LocalDateTime.now();
        setConversation(conversation);
    }

    //연관관계 편의 메서드
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        conversation.getTalk().add(this);
    }
}
