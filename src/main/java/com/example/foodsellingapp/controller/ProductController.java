package com.example.foodsellingapp.controller;

import com.example.foodsellingapp.model.dto.ProductDTO;
import com.example.foodsellingapp.model.entity.Product;
import com.example.foodsellingapp.repository.ProductRepository;
import com.example.foodsellingapp.repository.UserRepository;
import com.example.foodsellingapp.service.ProductService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    UserRepository  userRepository;
    @Autowired
    ProductRepository productRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("/get-by-id")
    public ResponseEntity<?> getProductById(){
        int intValue = 3;
        Long id = Long.valueOf(intValue);
        return ResponseEntity.ok(productService.getById(id));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/create-product")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO dto, @RequestParam MultipartFile images){
        productService.createProduct(dto,images );
        return ResponseEntity.ok("Create successfully");
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") List<MultipartFile> file) throws IOException {
        // Kiểm tra nếu tệp tin được upload không rỗng
        if (!file.isEmpty()) {
            // Đọc dữ liệu của tệp tin và lưu trữ vào thuộc tính 'data' của đối tượng Image
            byte[] imageData = file.get(0).getBytes();
            Product product = new Product();
            product.setData2(imageData);

            // Lưu đối tượng Image vào cơ sở dữ liệu
            productRepository.save(product);

            return "Upload thành công!";
        } else {
            return "Không có tệp tin được chọn!";
        }
    }



    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @PostMapping("/create-product-v2")
    public ResponseEntity<?> addProduct(@RequestParam("imageF") MultipartFile imageF, @RequestParam("price") double price,
                             @RequestParam("name") String name,
                             @RequestParam("quantity") Long quantity){
        productService.createProductV2(name,imageF,price,quantity);
        return ResponseEntity.ok("Create successfully");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    @GetMapping("get-by-name")
    public ResponseEntity<?> getByName(@Valid @RequestParam String productName){
        return ResponseEntity.ok(productService.findByProductName(productName));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.PATCH)
    @PatchMapping("/update-product")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDTO dto, @RequestParam Long productId){
        productService.updateProduct(productId,dto);
        return ResponseEntity.ok("Update successfully");
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    @DeleteMapping("/delete-product")
    public ResponseEntity<?> deleteProduct(@Valid @RequestParam Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Delete successfully");
    }

    // Hàm trích xuất ID của người mua hàng từ JWT
    private Long extractBuyerIdFromJwt(String jwt) {
        Claims claims = Jwts.parser().setSigningKey("bezKoderSecretKey").parseClaimsJws(jwt).getBody();
        String username = claims.getSubject();
        return userRepository.findByUsername(username).get().getId();
    }
}
