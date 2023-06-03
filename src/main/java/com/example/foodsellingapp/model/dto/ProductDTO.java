package com.example.foodsellingapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class ProductDTO {
    private String name;
    //private MultipartFile images;
    private Double price;
    private Long quantity;
}
