package com.example.foodsellingapp.service;

import com.example.foodsellingapp.exception.ProductException;
import com.example.foodsellingapp.model.dto.ProductDTO;
import com.example.foodsellingapp.model.entity.Product;
import com.example.foodsellingapp.repository.OrdersDetailRepository;
import com.example.foodsellingapp.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrdersDetailRepository orderDetailRepository;

    public Product createProductV2(String name, MultipartFile image,double price,Long quantity){
        Product product  = new Product();
        product.setQuantity(quantity);
        product.setName(name);
        product.setPrice(price);
        try{
            if(image != null){
                product.setImage1(Base64.getEncoder().encodeToString(image.getBytes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       return productRepository.save(product);
    }

    public Product createProduct(ProductDTO dto, MultipartFile images) {
        ModelMapper mapper = new ModelMapper();
        if(dto.getName()==null){
            throw new ProductException("Product name is null");
        }
        if(dto.getPrice()==null){
            throw new ProductException("Product price is null");
        }
        if(dto.getQuantity()==null){
            dto.setQuantity(0L);
        }
        if(productRepository.findByName(dto.getName()).isPresent()){
            throw new ProductException("Product is exist");
        }
        Product product = mapper.map(dto, Product.class);
        try{
            if(images!= null){
                product.setImage1(Base64.getEncoder().encodeToString(images.getBytes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productRepository.save(product);
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public ProductDTO getById(Long id){
        Product product = productRepository.findById(id).get();
        ProductDTO productDTO = new ProductDTO();
//        productDTO.setImage(Base64Utils.encodeToString(product.getImageData()));
        return productDTO;
    }

    public List<?> getBestProduct(){
//        // Lấy ngày hiện tại
//        Date today = new Date();
//
//        // Lấy ngày 15 ngày trước đó
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(today);
//        calendar.add(Calendar.DATE, -15);
//        Date date15DaysAgo = calendar.getTime();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before15Days = now.minusDays(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String today = now.format(formatter);
        String date15DaysAgo = before15Days.format(formatter);

        return orderDetailRepository.findTopProductsByVoteInLast15Day(date15DaysAgo,today);
    }

    public Product updateProduct(Long productId, ProductDTO dto){
        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("ERROR : Product is not found"));
        if(dto.getName()!=null){
            product.setName(dto.getName());
        }
        if(dto.getPrice()!=null){
            product.setPrice(dto.getPrice());
        }
//        if(dto.getImage()!= null){
//            product.setImage(dto.getImage());
//        }
        if(dto.getQuantity()!= null){
            product.setQuantity(dto.getQuantity());
        }
        return productRepository.save(product);
    }
    public void deleteProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product is not exist"));
        productRepository.delete(product);
    }

    public List<Product> findByProductName(String name){
        return productRepository.findProductLikeName(name);
    }

}
