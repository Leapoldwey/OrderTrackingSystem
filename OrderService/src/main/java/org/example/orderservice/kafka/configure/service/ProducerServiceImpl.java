package org.example.orderservice.kafka.configure.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.core.OrderEvent;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.kafka.configure.KafkaProducerConfigure;
import org.example.orderservice.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Logger log = LoggerFactory.getLogger(ProducerServiceImpl.class);

    @Override
    public void send(String topicName, String id, OrderDto orderDto) throws ExecutionException, InterruptedException {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
                KafkaProducerConfigure.TOPIC_NAME, id,
                new OrderEvent(
                        orderDto.getId(),
                        orderDto.getCustomerName(),
                        orderDto.getProduct(),
                        orderDto.getQuantity(),
                        orderDto.getStatus(),
                        orderDto.getCreatedAt()
                ))
                .whenComplete((r, e) -> {
                    if (e != null) {
                        log.error("Error sending message", e);
                    } else {
                        log.info("Message sent successfully");
                    }
                });
    }
}
