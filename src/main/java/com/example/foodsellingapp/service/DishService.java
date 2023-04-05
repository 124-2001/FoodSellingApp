package com.example.foodsellingapp.service;

import com.example.foodsellingapp.exception.DishException;
import com.example.foodsellingapp.model.dto.DishDTO;
import com.example.foodsellingapp.model.entity.Dish;
import com.example.foodsellingapp.repository.DishRepository;
import jdk.dynalink.linker.LinkerServices;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class DishService {
    @Autowired
    DishRepository dishRepository;

    public Dish createDish(DishDTO dto){
        ModelMapper mapper = new ModelMapper();
        if(dto.getName()==null){
            throw new DishException("Dish name is null");
        }
        if(dto.getPrice()==null){
            throw new DishException("Dish price is null");
        }
        Dish dish = mapper.map(dto,Dish.class);
        return dishRepository.save(dish);
    }

    public List<Dish> getAllDish(){
        return dishRepository.findAll();
    }

    public Dish updateDish(String dishName,DishDTO dto){
        Dish dish = dishRepository.findByName(dishName).orElseThrow(()-> new RuntimeException("ERROR : Dish is not found"));
        if(dto.getName()!=null){
            dish.setName(dto.getName());
        }
        if(dto.getPrice()!=null){
            dish.setPrice(dto.getPrice());
        }
        if(dto.getImage()!= null){
            dish.setImage(dto.getImage());
        }
        return dishRepository.save(dish);
    }

}
