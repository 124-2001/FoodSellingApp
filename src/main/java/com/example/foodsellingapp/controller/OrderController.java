package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.model.dto.OrderDetailDTO;
import com.example.foodsellingapp.model.eenum.StatusOrder;
import com.example.foodsellingapp.payload.response.MessageResponse;
import com.example.foodsellingapp.payload.response.OrderResponse;
import com.example.foodsellingapp.repository.OrderRepository;
import com.example.foodsellingapp.repository.UserRepository;
import com.example.foodsellingapp.service.OrderService;
import com.example.foodsellingapp.service.ProductService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductService productService;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllOrder(){
        return ResponseEntity.ok(orderService.getAll());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/get-by-created-date")
    public ResponseEntity<?> getAllOrderByCreatedDate(@Valid @RequestParam String date){
        return ResponseEntity.ok(orderService.getAllByCreateDate(date));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("get-detail-by-id")
    public ResponseEntity<?> getOrderById(@Valid @RequestParam Long orderId){
        return ResponseEntity.ok(orderService.getDetailOrder(orderId));
    }
    

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/get-order-by-customer")
    public ResponseEntity<?> getOrderByCustomerId(HttpServletRequest request){
        // Kiểm tra xem người dùng đã đăng nhập chưa, nếu chưa thì trả về lỗi 401
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Trích xuất ID của người mua hàng từ JWT
        String jwt = authorizationHeader.substring(7);
        Long customerId = extractBuyerIdFromJwt(jwt);
        return ResponseEntity.ok(orderService.getAllByCustomerId(customerId));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/get-all-by-status")
    public ResponseEntity<?> getAllByStatus(@Valid @RequestParam StatusOrder statusOrder){
        return ResponseEntity.ok(orderService.getAllByStatusOrder(statusOrder));
    }

    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("shipper/get-all-order-watting")
    public ResponseEntity<?> getAllOrderToDelivery(){
        return ResponseEntity.ok(orderService.getAllApproveOrder());
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/customer/get-by-status")
    public ResponseEntity<?> getAllByStatusAndIdCustomer(@Valid @RequestParam StatusOrder statusOrder,HttpServletRequest request){
        // Kiểm tra xem người dùng đã đăng nhập chưa, nếu chưa thì trả về lỗi 401
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Trích xuất ID của người mua hàng từ JWT
        String jwt = authorizationHeader.substring(7);
        Long customerId = extractBuyerIdFromJwt(jwt);
        return ResponseEntity.ok(orderService.getAllByStatusOrderAndCustomer(statusOrder,customerId));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(HttpServletRequest request) throws IOException {
        // Kiểm tra xem người dùng đã đăng nhập chưa, nếu chưa thì trả về lỗi 401
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Trích xuất ID của người mua hàng từ JWT
        String jwt = authorizationHeader.substring(7);
        Long customerId = extractBuyerIdFromJwt(jwt);
        return ResponseEntity.ok(orderService.createOrder(customerId));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOrder(@Valid @RequestParam Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Delete order successfully");
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody List<OrderDetailDTO> dtos,@RequestParam Long orderId) throws IOException {
        orderService.updateOrder(orderId,dtos);
        return ResponseEntity.ok("Update order successfully");
    }
    @PatchMapping("/feed-back")
    public ResponseEntity<?>feedBackOrder(@Valid @RequestParam Long orderId, @PathVariable String feedBack){
        if(orderService.feedbackOrder(orderId,feedBack)){
            return ResponseEntity.ok("Feedback successfully");
        }
        else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Feedback failed"));
        }
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.PATCH)
    @PatchMapping("/approve-order")
    public ResponseEntity<?> approveOrder(@Valid @RequestParam Long orderId){
        if(orderService.approveOrder(orderId)){
            return ResponseEntity.ok("Update order successfully");
        }
        else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Update failed"));
        }
    }
    @GetMapping("/get-best-product")
    public ResponseEntity<?> getBestProduct(){
        return ResponseEntity.ok(productService.getBestProduct());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/reject-order")
    public ResponseEntity<?> rejectOrder(@Valid @RequestParam Long orderId){
        if(orderService.rejectOrder(orderId)){
            return ResponseEntity.ok("Update order successfully");
        }
        else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Update failed"));
        }
    }

    // Hàm trích xuất ID của người mua hàng từ JWT
    private Long extractBuyerIdFromJwt(String jwt) {
        Claims claims = Jwts.parser().setSigningKey("bezKoderSecretKey").parseClaimsJws(jwt).getBody();
        String username = claims.getSubject();
        return userRepository.findByUsername(username).get().getId();
    }
}
