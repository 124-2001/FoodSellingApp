package com.example.foodsellingapp.service;

import com.example.foodsellingapp.model.dto.CartDTO;
import com.example.foodsellingapp.model.entity.Cart;
import com.example.foodsellingapp.model.entity.Product;
import com.example.foodsellingapp.repository.CartRepository;
import com.example.foodsellingapp.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    AuthService authService;

    public void createCart(CartDTO dto,Long idCustomer){
        Optional<Cart> cart = cartRepository.findByIdCustomerAndAndIdProduct(idCustomer, dto.getIdProduct());
        if(cart.isPresent()){
            Product product = productRepository.findById(dto.getIdProduct()).get();
            if(product.getQuantity()>=dto.getQuantity()){
                product.setQuantity(product.getQuantity()-dto.getQuantity());
                productRepository.save(product);
            }else {
                throw new RuntimeException("Hàng trong kho đã hết");
            }
            Long oldQuantity =cart.get().getQuantity();
            cart.get().setQuantity(oldQuantity+dto.getQuantity());
            cartRepository.save(cart.get());
        }else {
            Cart newCart = new Cart();
            newCart.setIdProduct(dto.getIdProduct());
            newCart.setIdCustomer(idCustomer);
            newCart.setQuantity(dto.getQuantity());
            Product product = productRepository.findById(dto.getIdProduct()).get();
            if(product.getQuantity()>=dto.getQuantity()){
                product.setQuantity(product.getQuantity()-dto.getQuantity());
                productRepository.save(product);
            }else {
                throw new RuntimeException("Hàng trong kho đã hết");
            }
            cartRepository.save(newCart);
        }
    }
    public Cart updateNumberItemInCart(Long cartId, CartDTO cartDTO){
        Cart cart = cartRepository.findById(cartId).get();
        Product product = productRepository.findById(cartDTO.getIdProduct()).get();
        Long quantityProductInStock = product.getQuantity();
        // cho phép nếu trong kho + số lượng cũ lớn hơn số lượng mới
        if(quantityProductInStock+cart.getQuantity()>=cartDTO.getQuantity()){
            cart.setQuantity(cartDTO.getQuantity());
            product.setQuantity(quantityProductInStock+cart.getQuantity()-cartDTO.getQuantity());
            productRepository.save(product);
            cartRepository.save(cart);
        }
        else {
            throw new RuntimeException("Số lượng hàng không đủ vui lòng thử lại");
        }
        return cart;
    }
    public void deleteCartItem(Long cartId){
        cartRepository.deleteById(cartId);
    }
    public List<Cart> getListCartByCustomer(Long idCustomer){
        return cartRepository.findAllByIdCustomer(idCustomer);
    }
    public void acceptCart(Long idCustomer){
        List<Cart> carts = getListCartByCustomer(idCustomer);
        cartRepository.deleteAll(carts);
    }
}
