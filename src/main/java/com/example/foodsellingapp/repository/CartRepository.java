package com.example.foodsellingapp.repository;

import com.example.foodsellingapp.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByIdCustomerAndAndIdProduct(Long idCusttomer, Long idProduct);
    List<Cart> findAllByIdCustomer(Long idCustomer);
}
