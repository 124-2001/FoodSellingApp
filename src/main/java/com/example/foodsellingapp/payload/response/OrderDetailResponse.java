package com.example.foodsellingapp.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponse {
    private String productName;
    private Double price;
    private Long quantity;
}
