package com.dompoo.onlineshopping.repository;

import com.dompoo.onlineshopping.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByConversation_IdOrderByCreatedAtAsc(Long convId);
}
