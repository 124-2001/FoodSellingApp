package com.example.foodsellingapp.repository;

import com.example.foodsellingapp.model.entity.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersDetailRepository extends JpaRepository<OrdersDetail,Long> {
    List<OrdersDetail> findAllByOrderId(Long orderId);
//    @Query("SELECT od.product.id, AVG(od.vote) as avg_vote FROM OrdersDetail od " +
//            "JOIN od.order o " +
//            "WHERE o.createdDate " +
//            "BETWEEN DATE_SUB(NOW(), INTERVAL 15 DAY) AND NOW() " +
//            "GROUP BY od.product.id " +
//            "ORDER BY avg_vote DESC")
//    List<Object[]> findTopProductsByVoteInLast15Days();

//    // xóa mẹ cái orderDetail đi run tạo lại
//    @Query(nativeQuery = true, value = "Select * from orders_detail where order_id =?1")
//    List<OrdersDetail> findOrderDetailsByOrderId(Long orderId);
}
