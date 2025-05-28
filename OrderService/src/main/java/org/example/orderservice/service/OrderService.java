package org.example.orderservice.service;

import org.example.orderservice.dto.OrderDto;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OrderService {
    void save(OrderDto orderDto) throws ExecutionException, InterruptedException;
    void delete(OrderDto orderDto);
    List<OrderDto> findAll();
    OrderDto findById(Long id);
}
