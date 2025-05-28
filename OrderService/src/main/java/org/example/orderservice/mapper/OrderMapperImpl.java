package org.example.orderservice.mapper;

import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.model.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class OrderMapperImpl implements OrderMapper {
    @Override
    public OrderDto mapTo(Order order) {
        return new OrderDto(
                order.getId(),
                order.getCustomerName(),
                order.getProduct(),
                order.getQuantity(),
                order.getStatus(),
                order.getCreatedAt());
    }

    @Override
    public Order mapTo(OrderDto orderDto) {
        return new Order(
                orderDto.getId(),
                orderDto.getCustomerName(),
                orderDto.getProduct(),
                orderDto.getQuantity(),
                orderDto.getStatus(),
                orderDto.getCreatedAt());
    }
}
