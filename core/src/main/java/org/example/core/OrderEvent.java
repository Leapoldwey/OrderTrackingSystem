package org.example.core;

import java.time.LocalDateTime;

public record OrderEvent(Long id,
                         String customerName,
                         String product,
                         int quantity,
                         String status,
                         LocalDateTime createdAt){}
