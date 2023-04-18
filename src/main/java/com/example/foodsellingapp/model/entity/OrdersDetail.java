package com.example.foodsellingapp.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Table(name = "orders_detail")
@Entity
@Getter
@Setter
public class OrdersDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = true)
    private Long id;
    @Column(name = "order_id",nullable = true)
    private Long orderId;
    @Column(name = "product_id",nullable = true)
    private Long productId;
    @Column(name = "QUANTITY", nullable = true)
    private Integer quantity;
    @Column(name = "vote",nullable = true)
    private Double vote;
    @Column(name = "feed_back")
    private String feedBack;
}
