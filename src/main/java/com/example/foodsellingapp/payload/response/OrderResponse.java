package com.example.foodsellingapp.payload.response;

import com.example.foodsellingapp.model.eenum.StatusOrder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private String customerName;
    private Timestamp createdDate;
    private Double totalPrice;
    private StatusOrder statusOrder;

    public OrderResponse(Long id, String customerName, Timestamp createdDate, Double totalPrice, StatusOrder statusOrder) {
        this.id = id;
        this.customerName = customerName;
        this.createdDate = createdDate;
        this.totalPrice = totalPrice;
        this.statusOrder = statusOrder;
    }
}
