package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.kafka.configure.KafkaProducerConfigure;
import org.example.orderservice.kafka.configure.service.ProducerService;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.model.Order;
import org.example.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final ProducerService producerService;
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional("transactionManager")
    public void save(OrderDto orderDto) throws ExecutionException, InterruptedException {
        Order order = null;

        if (orderDto.getId() != null && orderRepository.existsById(orderDto.getId())) {
            order = orderMapper.mapTo(orderDto);
        } else if (orderDto.getId() == null) {
            order = orderMapper.mapTo(orderDto);
            order.setCreatedAt(LocalDateTime.now());
        } else {
            throw new NoSuchElementException(
                    String.format("Заказ с идентификатором %d не найден!", orderDto.getId())
            );
        }

        try {
            orderRepository.save(order);
            orderDto.setId(order.getId());

            producerService.send(KafkaProducerConfigure.TOPIC_NAME, order.getId().toString(), orderDto);
        } catch (RuntimeException | ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(OrderDto orderDto) {
        if (orderRepository.existsById(orderDto.getId())) {
            orderRepository.deleteById(orderDto.getId());
        } else {
            throw new NoSuchElementException(
                    String.format("Заказ с идентификатором %d не найден!", orderDto.getId())
            );
        }
    }

    @Override
    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::mapTo)
                .toList();
    }

    @Override
    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Заказ с идентификатором %d не найден!", id))
                );

        return orderMapper.mapTo(order);
    }
}
