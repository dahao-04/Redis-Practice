package com.anhao.redis.controller;


import com.anhao.redis.model.Product;
import com.anhao.redis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // Cho phép gọi từ mọi frontend (CORS)
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public List<Product> getAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        return service.getAllProducts(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Product product = service.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.createProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return service.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
