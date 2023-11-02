package com.dompoo.onlineshopping.repository;

import com.dompoo.onlineshopping.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
