package org.example.orderservice.mapper;

import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.model.Order;

public interface OrderMapper {
    OrderDto mapTo(Order order);
    Order mapTo(OrderDto orderDto);
}
