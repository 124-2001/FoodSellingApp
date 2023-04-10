package com.example.foodsellingapp.model.entity;

import com.example.foodsellingapp.model.eenum.RankVote;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

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

    @Column(name = "PRICE", nullable = true, precision = 0)
    private Double price;
    @Column(name = "vote",nullable = true)
    private Double vote;
    @Column(name = "feed_back")
    private String feedBack;
}
