package org.example.kafkaconsumerservice.service;

import lombok.RequiredArgsConstructor;
import org.example.core.OrderEvent;
import org.example.kafkaconsumerservice.kafkaConfigure.ConsumerKafkaConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = ConsumerKafkaConfigure.TOPIC_NAME, groupId = ConsumerKafkaConfigure.GROUP_ID)
public class OrderEventService {
    private final Logger logger = LoggerFactory.getLogger(OrderEventService.class);

    @KafkaHandler
    public void handle(@Payload OrderEvent orderEvent, Acknowledgment ack) {
        try {
            logger.info("Создан новый заказ {}", orderEvent.id());
            logger.info("Наименование {}", orderEvent.product());
            logger.info("Позиций {}", orderEvent.product());
            logger.info("Состояние {}", orderEvent.status());
        } catch (DataIntegrityViolationException e) {
            logger.error("Error while accepting message: {}", orderEvent, e);
            throw e;
        }
        ack.acknowledge();
    }
}
