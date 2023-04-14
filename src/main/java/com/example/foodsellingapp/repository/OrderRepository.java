package com.example.foodsellingapp.repository;

import com.example.foodsellingapp.model.eenum.StatusOrder;
import com.example.foodsellingapp.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findById(Long id);
    List<Order> findAllByStatusOrder(StatusOrder statusOrder);
}
