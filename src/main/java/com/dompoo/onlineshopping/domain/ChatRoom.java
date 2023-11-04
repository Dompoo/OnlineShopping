package com.dompoo.onlineshopping.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public ChatRoom(Post post) {
        setPost(post);
    }

    //연관관계 편의 메서드
    private void setPost(Post post) {
        this.post = post;
        post.getChatRooms().add(this);
    }


}
