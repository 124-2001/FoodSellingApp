package com.example.foodsellingapp.repository;

import com.example.foodsellingapp.model.eenum.StatusOrder;
import com.example.foodsellingapp.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findById(Long id);
    List<Order> findAllByStatusOrder(StatusOrder statusOrder);
    List<Order> findAllByCustomerIdAndStatusOrder(Long id,StatusOrder statusOrder);
    List<Order> findAllByCustomerId(Long customerId);
//
//    @Query(value = "SELECT *" +
//            "FROM orders" +
//            "WHERE DATE(created_date) = ?1" +
//            "ORDER BY created_date", nativeQuery = true)
//    List<Object[]> findOrderByDate(String date);
    List<Order> findAllByCreatedDate(Timestamp date);
}
