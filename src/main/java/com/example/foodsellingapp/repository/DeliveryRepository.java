package com.example.foodsellingapp.repository;

import com.example.foodsellingapp.model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    Delivery findByOrderId(Long id);
}
