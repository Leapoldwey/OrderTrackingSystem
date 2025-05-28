package org.example.orderservice.kafka.configure.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.core.OrderEvent;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.kafka.configure.KafkaProducerConfigure;
import org.example.orderservice.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topicName, String id, OrderDto orderDto) throws ExecutionException, InterruptedException {
        ProducerRecord<String, Object> send = new ProducerRecord<>(
                KafkaProducerConfigure.TOPIC_NAME, id,
                new OrderEvent(
                        orderDto.getId(),
                        orderDto.getCustomerName(),
                        orderDto.getProduct(),
                        orderDto.getQuantity(),
                        orderDto.getStatus(),
                        orderDto.getCreatedAt()
                )
        );
        send.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        kafkaTemplate.send(send).get();
    }
}
