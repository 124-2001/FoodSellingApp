package com.example.foodsellingapp.service;

import com.example.foodsellingapp.model.dto.OrderDetailDTO;
import com.example.foodsellingapp.model.eenum.StatusOrder;
import com.example.foodsellingapp.model.entity.Order;
import com.example.foodsellingapp.model.entity.OrdersDetail;
import com.example.foodsellingapp.model.entity.Product;
import com.example.foodsellingapp.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrdersDetailRepository orderDetailRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

//    @Value("${DISTANCE_MATRIX_API_URL}")
//    private String DISTANCE_MATRIX_API_URL;
//
//    @Value("${API_KEY}")
//    private String API_KEY;
//
//    @Value("${address}")
//    private String address;
    private static final String DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String API_KEY = "AIzaSyA0nbhG4Pp90jmkZapt5NntyQucVKE-pkY";
    private static final String address ="814 láng";


    public List<Order> getAll(){
        return orderRepository.findAll();
    }
    public Order getById(Long orderId){
        return orderRepository.findById(orderId).get();
    }

    public Order createOrder(List<OrderDetailDTO> dtos, Long customerId) throws IOException {
        if(!checkProductExist(dtos)){
            throw new RuntimeException("Product is not exist. Please try again");
        }
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Order order  = new Order();
        order.setCustomerId(customerId);
        order.setCreatedDate(currentTimestamp);
        order.setStatusOrder(StatusOrder.WAITING);
        orderRepository.save(order);
        if(dtos.isEmpty()){
            throw new RuntimeException("Order is empty");
        }
        double totalPrice=0;
        for (OrderDetailDTO dto : dtos) {
            Optional<Product> product = productRepository.findById(dto.getProductId());
            if(product.isPresent()){
                OrdersDetail orderDetail = new OrdersDetail();
                orderDetail.setProductId(dto.getProductId());
                if(product.get().getQuantity()<dto.getQuantity()){
                    throw new RuntimeException("Product : '"+product.get().getName()+"' is currently out of stock");
                }
                else {
                    orderDetail.setQuantity(dto.getQuantity());
                    product.get().setQuantity(product.get().getQuantity()-dto.getQuantity());
                    productRepository.save(product.get());
                }
                orderDetail.setOrderId(order.getId());
//                orderDetail.setPrice(product.get().getPrice());
                totalPrice+=dto.getQuantity()* product.get().getPrice();
                orderDetailRepository.save(orderDetail);
            }

        }
        //lấy tiền vận chuyển theo khoảng cách
        double deliveryPrice = getDeliveryPrice(userRepository.findById(customerId).get().getAddress());
        totalPrice+=deliveryPrice;
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long orderId){
        Order order= orderRepository.findById(orderId).get();
        if(order.getStatusOrder()!= StatusOrder.WAITING){
            throw new  RuntimeException("Order is APPROVE . Can't cancel order");
        }
        List<OrdersDetail> orderDetails = orderDetailRepository.findAllByOrderId(orderId);
        if(!orderDetails.isEmpty()){
            for (OrdersDetail orderDetail : orderDetails) {
                Product product = productRepository.findById(orderDetail.getProductId()).get();
                product.setQuantity(product.getQuantity()+orderDetail.getQuantity());
                productRepository.save(product);
                orderDetailRepository.delete(orderDetail);
            }
        }
        orderRepository.delete(order);
    }

    public void updateOrder(long orderId,List<OrderDetailDTO> dtos) throws IOException {
        if(!checkProductExist(dtos)){
            throw new RuntimeException("Product is not exist. Please try again");
        }
        Optional<Order> order= orderRepository.findById(orderId);
        if(!order.isPresent()){
            throw new RuntimeException("Order is not exist");
        }
        else {
            if(order.get().getStatusOrder()==StatusOrder.WAITING){
                ModelMapper mapper = new ModelMapper();
                double totalPrice =0;
                // xoa detail cu va them lai quantity cho product
                List<OrdersDetail> detailsOld = orderDetailRepository.findAllByOrderId(orderId);
                for (OrdersDetail ordersDetail : detailsOld) {
                    Product product = productRepository.findById(ordersDetail.getProductId()).get();
                    product.setQuantity(product.getQuantity()+ordersDetail.getQuantity());
                    productRepository.save(product);
                    orderDetailRepository.delete(ordersDetail);
                }
                //add lai du lieu moi
                for (OrderDetailDTO dto : dtos) {
                    OrdersDetail ordersDetail = mapper.map(dto,OrdersDetail.class);
                    ordersDetail.setOrderId(orderId);
                    Product product = productRepository.findById(dto.getProductId()).get();
                    if(product.getQuantity()<dto.getQuantity()){
                        throw new RuntimeException("Product : '"+product.getName()+"' is currently out of stock");
                    }
                    else {
                        product.setQuantity(product.getQuantity()-dto.getQuantity());
                    }
                    totalPrice+= dto.getQuantity()*product.getPrice();
                    productRepository.save(product);
                    orderDetailRepository.save(ordersDetail);
                }
                double deliveryPrice = getDeliveryPrice(userRepository.findById(order.get().getCustomerId()).get().getAddress());
                totalPrice+=deliveryPrice;
                order.get().setTotalPrice(totalPrice);
                orderRepository.save(order.get());
            }
            else {
                throw new RuntimeException("Status order is :"+order.get().getStatusOrder()+" can't update");
            }
        }
    }

    public void feedbackOrder(long orderDetailId,String feedback){
        OrdersDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(()->
                new RuntimeException("The product is not included in the order"));
        orderDetail.setFeedBack(feedback);
        orderDetailRepository.save(orderDetail);
    }

    public boolean approveOrder(long orderId){
        Order order= orderRepository.findById(orderId).orElseThrow(
                ()->new RuntimeException("Order is not exist"));
        if(order.getStatusOrder()==StatusOrder.WAITING){
            order.setStatusOrder(StatusOrder.APPROVED);
            return true;
        }
        return false;
    }
    public boolean checkProductExist(List<OrderDetailDTO> dtos){
        for (OrderDetailDTO dto : dtos) {
            if(!productRepository.findById(dto.getProductId()).isPresent()){
                return false;
            }
        }
        return true;
    }

    public double getDeliveryPrice(String destination) throws IOException {
        double distance = getDistance(destination);
        System.out.println(distance);
        if(distance<= 2.5){
            return 0;
        }
        else if (2.5<distance && distance<=5){
            return 25;
        }
        else if (distance>20){
            return 40;
        }
        else {
            return 5000*distance;
        }
    }

    public double getDistance(String destination) throws IOException {
        String url = DISTANCE_MATRIX_API_URL + "?origins=" + URLEncoder.encode(address, "UTF-8")
                + "&destinations=" + URLEncoder.encode(destination, "UTF-8")
                + "&key=" + API_KEY;

        URL urlObject = new URL(url);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(urlObject.openStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject jsonObject = new JSONObject(response.toString());

            String status = jsonObject.getString("status");

            if (!"OK".equals(status)) {
                throw new IOException("API returned status code " + status);
            }

            JSONObject element = jsonObject.getJSONArray("rows").getJSONObject(0)
                    .getJSONArray("elements").getJSONObject(0);
//            String distanceText = element.getJSONObject("distance").getString("text");
            double distanceValue = element.getJSONObject("distance").getDouble("value") / 1000.0;

            return distanceValue;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
