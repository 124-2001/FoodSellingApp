package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.model.entity.RefreshToken;
import com.example.foodsellingapp.payload.request.LoginRequest;
import com.example.foodsellingapp.payload.response.JwtResponse;
import com.example.foodsellingapp.repository.RoleRepository;
import com.example.foodsellingapp.repository.UserRepository;
import com.example.foodsellingapp.security.RefreshTokenService;
import com.example.foodsellingapp.security.UserDetailsImpl;
import com.example.foodsellingapp.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class TestController {


//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//
//
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
//
//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password, Model model,
//                        HttpServletResponse response, HttpSession session) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        username,
//                        password
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = tokenProvider.generateToken(authentication);
//        User user = userService.getUserByUsername(username);
//
//        session.setAttribute("jwt", jwt);
//        session.setAttribute("currentUser", user);
//
//        return "redirect:/";
//    }
}
