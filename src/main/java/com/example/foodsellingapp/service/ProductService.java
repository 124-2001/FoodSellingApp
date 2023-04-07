package com.example.foodsellingapp.service;

import com.example.foodsellingapp.exception.ProductException;
import com.example.foodsellingapp.model.dto.ProductDTO;
import com.example.foodsellingapp.model.entity.Product;
import com.example.foodsellingapp.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product createProduct(ProductDTO dto){
        ModelMapper mapper = new ModelMapper();
        if(dto.getName()==null){
            throw new ProductException("Dish name is null");
        }
        if(dto.getPrice()==null){
            throw new ProductException("Dish price is null");
        }
        Product product = mapper.map(dto, Product.class);
        return productRepository.save(product);
    }

    public List<Product> getAllDish(){
        return productRepository.findAll();
    }

    public Product updateProduct(String dishName, ProductDTO dto){
        Product product = productRepository.findByName(dishName).orElseThrow(()-> new RuntimeException("ERROR : Dish is not found"));
        if(dto.getName()!=null){
            product.setName(dto.getName());
        }
        if(dto.getPrice()!=null){
            product.setPrice(dto.getPrice());
        }
        if(dto.getImage()!= null){
            product.setImage(dto.getImage());
        }
        return productRepository.save(product);
    }

}
