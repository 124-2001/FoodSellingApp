package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.model.dto.ProductDTO;
import com.example.foodsellingapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(productService.getAllDish());
    }

    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO dto){
        productService.createProduct(dto);
        return ResponseEntity.ok("Create successfully");
    }
    @PatchMapping("/update-product")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDTO dto, @RequestParam String productName){
        productService.updateProduct(productName,dto);
        return ResponseEntity.ok("Update successfully");
    }
    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@Valid @RequestParam Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Delete successfully");
    }
}
