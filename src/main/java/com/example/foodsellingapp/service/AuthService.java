package com.example.foodsellingapp.service;

import com.example.foodsellingapp.model.dto.UserDTO;
import com.example.foodsellingapp.model.entity.User;
import com.example.foodsellingapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.awt.desktop.OpenFilesEvent;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AuthService {
    @Autowired
    UserRepository userRepository;

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
}
