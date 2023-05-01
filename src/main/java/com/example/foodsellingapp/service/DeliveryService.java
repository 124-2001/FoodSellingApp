package com.example.foodsellingapp.service;

import com.example.foodsellingapp.model.eenum.RoleName;
import com.example.foodsellingapp.model.eenum.StatusDelivery;
import com.example.foodsellingapp.model.eenum.StatusOrder;
import com.example.foodsellingapp.model.entity.Delivery;
import com.example.foodsellingapp.model.entity.Order;
import com.example.foodsellingapp.model.entity.User;
import com.example.foodsellingapp.repository.DeliveryRepository;
import com.example.foodsellingapp.repository.OrderRepository;
import com.example.foodsellingapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Service
@Slf4j
@Transactional
public class DeliveryService {
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;

    public List<Delivery> getAll(){
        return deliveryRepository.findAll();
    }

    public Delivery getShipper(Long shipperId , Long orderId){
        User shipper = userRepository.findById(shipperId).orElseThrow(
                ()-> new RuntimeException("Shipper is not exist")
        );
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new RuntimeException("Order is not exist")
        );
        if(order.getStatusOrder()!= StatusOrder.APPROVED){
            throw new RuntimeException("Order is not ready . Please try again!");
        }
        Delivery delivery = new Delivery();
        delivery.setShipperId(shipperId);
        delivery.setOrderId(orderId);
        delivery.setStatusDelivery(StatusDelivery.PROCESSING);
        order.setStatusOrder(StatusOrder.DELIVERY);
        orderRepository.save(order);
        return deliveryRepository.save(delivery);
    }

    public Delivery updateDelivery(Long deliveryId, StatusDelivery statusDelivery){
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                ()-> new RuntimeException("Delivery is not exist")
        );
        delivery.setStatusDelivery(statusDelivery);
        return deliveryRepository.save(delivery);
    }
}
