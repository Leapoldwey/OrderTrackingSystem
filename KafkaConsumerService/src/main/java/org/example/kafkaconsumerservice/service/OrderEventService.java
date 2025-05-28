package org.example.kafkaconsumerservice.service;

import lombok.RequiredArgsConstructor;
import org.example.core.OrderEvent;
import org.example.kafkaconsumerservice.kafkaConfigure.ConsumerKafkaConfigure;
import org.example.kafkaconsumerservice.persistence.entity.Message;
import org.example.kafkaconsumerservice.persistence.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = ConsumerKafkaConfigure.TOPIC_NAME, groupId = ConsumerKafkaConfigure.GROUP_ID)
public class OrderEventService {
    private final MessageRepository messageRepository;
    private final Logger logger = LoggerFactory.getLogger(OrderEventService.class);

    @KafkaHandler
    @Transactional
    public void handle(@Payload OrderEvent orderEvent, @Header(name = "messageId") String messageId,
                       Acknowledgment ack) {
        Message message = messageRepository.findByMessage(messageId);

        if (message != null) {
            logger.info("Duplicate message found for message id: {}", messageId);
            ack.acknowledge();
            return;
        }

        try {
            logger.info("Создан новый заказ {}", orderEvent.id());
            logger.info("Наименование {}", orderEvent.product());
            logger.info("Позиций {}", orderEvent.product());
            logger.info("Состояние {}", orderEvent.status());

            Message newMessage = new Message();
            messageRepository.save(newMessage);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error while accepting message: {}", orderEvent, e);
            throw e;
        }
        ack.acknowledge();
    }
}
