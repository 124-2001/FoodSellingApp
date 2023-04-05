package com.example.foodsellingapp.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table
@Entity
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code_discount")
    private String codeDiscount;
    @Column(name = "code_value")
    private double codeValue;
    @Column(name = "use_number")
    private Integer useNumber;
}
