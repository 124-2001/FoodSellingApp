package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/order/get-all")
    public ResponseEntity<?> getAllOrder(){
        return ResponseEntity.ok(orderService.getAll());
    }


}
