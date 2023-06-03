package com.example.foodsellingapp.repository;

import com.example.foodsellingapp.model.entity.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrdersDetailRepository extends JpaRepository<OrdersDetail,Long> {
    List<OrdersDetail> findAllByOrderId(Long orderId);

    @Query(value = "SELECT od.product_id, AVG(od.vote) as avg_vote FROM orders_detail od " +
            "            JOIN orders o " +
            "            WHERE o.created_date " +
            "            BETWEEN ?1 AND ?2  " +
            "            GROUP BY od.product_id " +
            "            ORDER BY avg_vote DESC", nativeQuery = true)
    List<Object[]> findTopProductsByVoteInLast15Day(String startDate, String endDate);



}
