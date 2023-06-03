package com.example.foodsellingapp.service;

import com.example.foodsellingapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
@Transactional
public class AuthService {
    @Autowired
    UserRepository userRepository;

    public Long getIdByJwt(HttpServletRequest  request){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        // Trích xuất ID của người mua hàng từ JWT
        String jwt = authorizationHeader.substring(7);
        Long customerId = extractBuyerIdFromJwt(jwt);
        return customerId;
    }

    // Hàm trích xuất ID của người mua hàng từ JWT
    public Long extractBuyerIdFromJwt(String jwt) {
        Claims claims = Jwts.parser().setSigningKey("bezKoderSecretKey").parseClaimsJws(jwt).getBody();
        String username = claims.getSubject();
        return userRepository.findByUsername(username).get().getId();
    }
}
