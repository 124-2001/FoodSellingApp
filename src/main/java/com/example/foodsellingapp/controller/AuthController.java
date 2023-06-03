package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.exception.TokenRefreshException;
import com.example.foodsellingapp.model.dto.LoginInfo;
import com.example.foodsellingapp.model.eenum.RoleName;
import com.example.foodsellingapp.model.entity.RefreshToken;
import com.example.foodsellingapp.model.entity.Role;
import com.example.foodsellingapp.model.entity.User;
import com.example.foodsellingapp.payload.request.LoginRequest;
import com.example.foodsellingapp.payload.request.RefreshTokenRequest;
import com.example.foodsellingapp.payload.request.SignupRequest;
import com.example.foodsellingapp.payload.request.TokenRefreshRequest;
import com.example.foodsellingapp.payload.response.*;
import com.example.foodsellingapp.repository.RoleRepository;
import com.example.foodsellingapp.repository.UserRepository;
import com.example.foodsellingapp.security.RefreshTokenService;
import com.example.foodsellingapp.security.UserDetailsImpl;
import com.example.foodsellingapp.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
//
//    @Autowired
//    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
//        return ResponseEntity.ok(jwt);
        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(),userDetails.getEmail(),userDetails.getFirstName(),userDetails.getLastName(), roles));
//        LoginResponse response = authService.login(loginRequest);
//        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword())
                , signUpRequest.getEmail()
                ,signUpRequest.getAddress()
                ,signUpRequest.getFirstname(),signUpRequest.getLastname(),signUpRequest.getPhone());

//        Set<String> strRoles = signUpRequest.getRole();
//        Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//                        break;
//                    case "customer":
//                        Role customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(customerRole);
//                        break;
//                    case "shipper":
//                        Role shipperRole = roleRepository.findByName(RoleName.ROLE_SHIPPER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(shipperRole);
//                        break;
//                    default:
//                        throw new RuntimeException("Error: Role is not found.");
//                }
//            });
//        }
//
//        user.setRoles(roles);
        Optional<Role> role = roleRepository.findByName(RoleName.ROLE_CUSTOMER);
        List<Role> roles = new ArrayList<>();
        roles.add(role.get());
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//        authService.signup(signUpRequest);
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
//        RefreshTokenResponse response = authService.refreshToken(request);
//        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
//        LoginInfo userDetails = (LoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = userDetails.getUserId();
//        refreshTokenService.deleteByUserId(userId);
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
