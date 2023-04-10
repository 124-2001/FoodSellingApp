package com.example.foodsellingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodSellingAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodSellingAppApplication.class, args);
    }
//
//    @Autowired
//    private OrdersDetailRepository ordersDetailRepository;
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        List<Order> orders = orderRepository.findAll();
//        if(orders.isEmpty()){
//            System.out.println("Null order");
//        }
//        //List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailsByOrderId(1L);
//        List<OrdersDetail> orderDetails = ordersDetailRepository.findOrderDetailsByOrderId(1L);
//        if(orderDetails.isEmpty()){
//            System.out.println("Null");
//        }
//    }
}
