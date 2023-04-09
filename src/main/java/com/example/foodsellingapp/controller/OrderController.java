package com.example.foodsellingapp.controller;

import com.auth0.jwt.JWT;
import com.example.foodsellingapp.model.dto.OrderDetailDTO;
import com.example.foodsellingapp.repository.UserRepository;
import com.example.foodsellingapp.service.OrderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllOrder(){
        return ResponseEntity.ok(orderService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody List<OrderDetailDTO> dtos, HttpServletRequest request) throws IOException {
        // Kiểm tra xem người dùng đã đăng nhập chưa, nếu chưa thì trả về lỗi 401
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Trích xuất ID của người mua hàng từ JWT
        String jwt = authorizationHeader.substring(7);
        Long customerId = extractBuyerIdFromJwt(jwt);
        return ResponseEntity.ok(orderService.createOrder(dtos,customerId));
    }
    // Hàm trích xuất ID của người mua hàng từ JWT
    private Long extractBuyerIdFromJwt(String jwt) {
        Claims claims = Jwts.parser().setSigningKey("bezKoderSecretKey").parseClaimsJws(jwt).getBody();
        String username = claims.getSubject();
        return userRepository.findByUsername(username).get().getId();
    }
}
