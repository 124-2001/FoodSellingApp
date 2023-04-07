package com.example.foodsellingapp.model.entity;

import com.example.foodsellingapp.model.eenum.RankVote;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "detail_order")
@Entity
@Getter
@Setter
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = true)
    private Long id;
    @Column(name = "order_id",nullable = true)
    private Long orderId;
    @Column(name = "product_id",nullable = true)
    private Long productId;
    @Basic
    @Column(name = "QUANTITY", nullable = true)
    private Integer quantity;
    @Basic
    @Column(name = "PRICE", nullable = true, precision = 0)
    private Long price;
    @Column(name = "vote",nullable = true)
    private RankVote vote;
    @Column(name = "feed_back")
    private String feedBack;
}
