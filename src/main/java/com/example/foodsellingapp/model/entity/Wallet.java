package com.example.foodsellingapp.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Table
@Entity
@Getter
@Setter
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double point;
}
