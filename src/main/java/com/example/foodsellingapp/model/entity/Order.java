package com.example.foodsellingapp.model.entity;

import com.example.foodsellingapp.model.eenum.StatusOrder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "orders")
@Entity
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column(name = "total_price")
    private Long totalPrice;
    @Column(name = "status_order")
    private StatusOrder statusOrder;
//    private Long shopId;
}
