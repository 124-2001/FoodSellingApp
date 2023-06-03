package com.example.foodsellingapp.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table
@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    @Column(name = "image_1", columnDefinition = "LONGBLOB")
    private String image1;
//    @Lob
//    @Column(name = "image_2", columnDefinition = "LONGBLOB")
//    private String image2;
//    @Lob
//    @Column(name = "image_3", columnDefinition = "LONGBLOB")
//    private String image3;
    @Lob
    private byte[] data2;

    private Double price;
    private Long quantity;
}
