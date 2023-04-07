package com.example.foodsellingapp.service;

import com.example.foodsellingapp.model.dto.OrderDetailDTO;
import com.example.foodsellingapp.model.eenum.StatusOrder;
import com.example.foodsellingapp.model.entity.Order;
import com.example.foodsellingapp.model.entity.OrderDetail;
import com.example.foodsellingapp.repository.ProductRepository;
import com.example.foodsellingapp.repository.OrderDetailRepository;
import com.example.foodsellingapp.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    ProductRepository productRepository;


    public Order createOrder(List<OrderDetailDTO> dtos, Long customerId){
        ModelMapper mapper = new ModelMapper();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Order order  = new Order();
        order.setCustomerId(customerId);
        order.setCreatedDate(currentTimestamp);
        order.setStatusOrder(StatusOrder.WAITING);
        orderRepository.save(order);
        if(dtos.isEmpty()){
            throw new RuntimeException("Order is empty");
        }
        long totalPrice=0;
        for (OrderDetailDTO dto : dtos) {
            OrderDetail orderDetail = mapper.map(dto,OrderDetail.class);
            orderDetail.setOrderId(order.getId());
            totalPrice+=dto.getQuantity()* productRepository.findById(Math.toIntExact(dto.getDishId())).get().getPrice();
            orderDetailRepository.save(orderDetail);
        }
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }
    public void cancelOrder(long orderId){
        Order order= orderRepository.findById(Math.toIntExact(orderId)).get();
        if(order.getStatusOrder()!= StatusOrder.WAITING){
            throw new  RuntimeException("Order is APPROVE . Can't cancel order");
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findByProductId(orderId);
        orderDetailRepository.deleteAll(orderDetails);
        orderRepository.delete(order);
    }
    public void updateOrder(long orderId,List<OrderDetailDTO> dtos){
        Order order= orderRepository.findById(Math.toIntExact(orderId)).get();
        ModelMapper mapper = new ModelMapper();
        if(order.getStatusOrder()== StatusOrder.WAITING){
            orderDetailRepository.deleteAll(orderDetailRepository.findByProductId(orderId));
            for (OrderDetailDTO dto : dtos) {
                OrderDetail detail = mapper.map(dto,OrderDetail.class);
                detail.setOrderId(orderId);
                orderDetailRepository.save(detail);
            }
        }
        else {
            throw new  RuntimeException("Order is APPROVE or REJECT . Can't update order");
        }
    }

}
