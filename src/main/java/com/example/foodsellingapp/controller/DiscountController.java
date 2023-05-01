package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discount")
public class DiscountController {
    @Autowired
    DiscountService discountService;


}
