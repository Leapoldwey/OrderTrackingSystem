package org.example.kafkaconsumerservice.persistence.repository;

import org.example.kafkaconsumerservice.persistence.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findByMessage(String messageId);
}
