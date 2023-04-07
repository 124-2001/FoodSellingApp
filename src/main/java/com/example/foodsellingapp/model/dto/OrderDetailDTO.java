package com.example.foodsellingapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Long orderId;
    private Long dishId;
    private Integer quantity;
}
