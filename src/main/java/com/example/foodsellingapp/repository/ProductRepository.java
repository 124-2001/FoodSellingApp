package com.example.foodsellingapp.repository;

import com.example.foodsellingapp.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);
    Optional<Product> findById(Long id);

    @Query(value = "SELECT * FROM product WHERE product.name LIKE %:name%", nativeQuery = true)
    List<Product> findProductLikeName(@Param("name") String name);
}
