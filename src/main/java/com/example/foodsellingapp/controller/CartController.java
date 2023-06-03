package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.model.entity.Product;
import com.example.foodsellingapp.service.AuthService;
import com.example.foodsellingapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    AuthService authService;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/get-item-to-cart")
    public ResponseEntity<?> getToCart() {

    }
}
