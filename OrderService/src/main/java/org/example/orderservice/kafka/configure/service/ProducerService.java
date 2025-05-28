package org.example.orderservice.kafka.configure.service;

import org.example.core.OrderEvent;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.model.Order;

import java.util.concurrent.ExecutionException;

public interface ProducerService {
    void send(String topicName, String id, OrderDto orderDto) throws ExecutionException, InterruptedException;
}
