package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {
    @Autowired
    DeliveryService deliveryService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getDeliveryByData(){
        return ResponseEntity.ok(deliveryService.getAll());
    }
    @PostMapping("/create-delevery")
    public ResponseEntity<?> createDelivery(@RequestParam Long shipperId,@RequestParam Long orderId){
        return ResponseEntity.ok(deliveryService.getShipper(shipperId,orderId));
    }
}
