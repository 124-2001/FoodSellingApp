package com.example.foodsellingapp.service;

import com.example.foodsellingapp.model.entity.Discount;
import com.example.foodsellingapp.repository.DiscountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;

    public Discount createDiscount(double value,int useNumber){
        String code;
        do{
            code = randomCode();
        }while (discountRepository.findByCodeDiscount(code)!=null);
        Discount discount = new Discount();
        discount.setCodeDiscount(code);
        discount.setCodeValue(value);
        discount.setUseNumber(useNumber);
        return discountRepository.save(discount);
    }
    public String randomCode(){
        String str = "ABCDEFGHYJKQMNL123456789";
        String randomcode = "";
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            int n = random.nextInt(str.length());
            randomcode += str.charAt(n);
        }
        return randomcode;
    }
}
