package com.example.foodsellingapp.service;

import com.example.foodsellingapp.model.dto.*;
import com.example.foodsellingapp.model.eenum.RoleName;
import com.example.foodsellingapp.model.entity.Role;
import com.example.foodsellingapp.model.entity.User;
import com.example.foodsellingapp.payload.request.LoginRequest;
import com.example.foodsellingapp.payload.request.RefreshTokenRequest;
import com.example.foodsellingapp.payload.request.SignupRequest;
import com.example.foodsellingapp.payload.response.LoginResponse;
import com.example.foodsellingapp.payload.response.MessageResponse;
import com.example.foodsellingapp.payload.response.RefreshTokenResponse;
import com.example.foodsellingapp.repository.RoleRepository;
import com.example.foodsellingapp.repository.UserRepository;
import com.example.foodsellingapp.security.constant.ErrorConstant;
import com.example.foodsellingapp.security.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.awt.desktop.OpenFilesEvent;
import java.util.*;

@Service
@Transactional
@Slf4j
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    public User getUser(Long userId){
        return userRepository.findById(userId).get();
    }

    public void blockUser(Long userId){
        User user =userRepository.findById(userId).orElseThrow(()->
                new RuntimeException("User is not exist"));
        user.setIsEnable(1);
    }

    // nhap lai password hoac gui ma otp qua email de xac thuc
    public User updateInfoUser(Long userId, UserDTO dto){
        User user =userRepository.findById(userId).orElseThrow(()->
                new RuntimeException("User is not exist"));
        user.setAddress(dto.getAddress());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public void signup(SignupRequest signUpRequest){
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

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
        Optional<Role> role = roleRepository.findByName(RoleName.ROLE_CUSTOMER);
        List<Role> roles = new ArrayList<>();
        roles.add(role.get());
        user.setRoles(roles);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        LoginResponse response = new LoginResponse();
        User loginUser = userRepository.findByUsername(request.getUsername()).get();
        if (Objects.isNull(loginUser) || !passwordEncoder.matches(request.getPassword(), loginUser.getPassword())) {
            throw new IllegalArgumentException("Invalid user name or password");
        }
        log.info("{} pass valid db ec_uaa", request.getUsername());

        JWTPayloadDto payloadDto = new JWTPayloadDto();
        payloadDto.setUserId(loginUser.getId());
        payloadDto.setRoleNames(loginUser.getRoles());
        response.setToken(buildTokenInfo(payloadDto));

        // user data
        response.setUserId(loginUser.getId());
        response.setFirstName(loginUser.getFirstName());
        response.setLastName(loginUser.getLastName());
        response.setFullName(loginUser.getFirstName()+" "+ loginUser.getLastName());

        return response;
    }

    private TokenInfo buildTokenInfo(JWTPayloadDto payloadDto) {
        long targetTime = new Date().getTime();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setIdToken(UUID.randomUUID().toString());

        // access token
        GeneratedTokenDto tokenDto = jwtTokenUtil.generateAccessToken(payloadDto, targetTime, tokenInfo.getIdToken());
        tokenInfo.setAccessToken(tokenDto.getGeneratedToken());
        tokenInfo.setAccessTokenExpireTime(tokenDto.getExpireTime());
 
        // refresh token
        tokenDto = jwtTokenUtil.generateRefreshToken(targetTime);
        tokenInfo.setRefreshToken(tokenDto.getGeneratedToken());
        tokenInfo.setRefreshTokenExpireTime(tokenDto.getExpireTime());
        return tokenInfo;
    }

    public LoginInfo buildLoginInfo(JWTPayloadDto payloadDto) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserId(payloadDto.getUserId());
        User user = userRepository.findById(payloadDto.getUserId()).get();
        loginInfo.setFirstName(user.getFirstName());
        loginInfo.setLastName(user.getLastName());
        loginInfo.setFullName(user.getFirstName()+" "+ user.getLastName());
        loginInfo.setRoleName(payloadDto.getRoleNames());
        return loginInfo;
    }

    public List<GrantedAuthority> getGrantedAuthories(List<String> roleNames) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roleNames.forEach(roleName -> {
            authorities.add(new SimpleGrantedAuthority(roleName));
        });
        return authorities;
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshTokenResponse response = new RefreshTokenResponse();

        // verify refresh token
        String errorCode = jwtTokenUtil.verifyJWTRefreshToken(request.getRefreshToken());
        if(StringUtils.isNotEmpty(errorCode)) {
            response.setErrorCode(errorCode);
            return response;
        }

        // verify access token
        errorCode = jwtTokenUtil.verifyJWTAccessToken(request.getAccessToken());
        if(StringUtils.isNotEmpty(errorCode) && !ErrorConstant.ERR_TOKEN_001.equals(errorCode)) {
            response.setErrorCode(errorCode);
            return response;
        }

        // refresh token
        long targetTime = new Date().getTime();
        JWTPayloadDto payloadDto;
        try {
            payloadDto = jwtTokenUtil.getPayloadFromJWT(request.getAccessToken());
        } catch (Exception ex) {
            response.setErrorCode(ErrorConstant.ERR_COMMON_001);
            return response;
        }

        // access token
        GeneratedTokenDto newToken = jwtTokenUtil.generateAccessToken(payloadDto, targetTime, request.getIdToken());
        response.setAccessToken(newToken.getGeneratedToken());
        response.setAccessTokenExpireTime(newToken.getExpireTime());

        // refresh token
        newToken = jwtTokenUtil.generateRefreshToken(targetTime);
        response.setRefreshToken(newToken.getGeneratedToken());
        response.setRefreshTokenExpireTime(newToken.getExpireTime());
        return response;
    }
}
