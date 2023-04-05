package com.example.foodsellingapp.repository;

import com.example.foodsellingapp.model.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish,Integer> {
    Optional<Dish> findByName(String name);
}
