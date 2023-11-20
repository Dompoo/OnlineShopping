package com.dompoo.onlineshopping.repository;

import com.dompoo.onlineshopping.domain.ChatRoom;
import com.dompoo.onlineshopping.domain.Post;
import com.dompoo.onlineshopping.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


    Optional<ChatRoom> findByUserAndPost(User user, Post post);
}
