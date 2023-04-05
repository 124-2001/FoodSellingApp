package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.model.dto.DishDTO;
import com.example.foodsellingapp.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/dish")
public class DishController {
    @Autowired
    DishService dishService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(dishService.getAllDish());
    }

    @PostMapping("/create-dish")
    public ResponseEntity<?> createDish(@Valid @RequestBody DishDTO dto){
        dishService.createDish(dto);
        return ResponseEntity.ok("Create successfully");
    }
    @PatchMapping("/update-dish")
    public ResponseEntity<?> updateDish(@Valid @RequestBody DishDTO dto,@RequestParam String dishName){
        dishService.updateDish(dishName,dto);
        return ResponseEntity.ok("Update successfully");
    }
}
