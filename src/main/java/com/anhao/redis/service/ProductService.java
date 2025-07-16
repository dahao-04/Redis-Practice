package com.anhao.redis.service;

import com.anhao.redis.model.Product;
import com.anhao.redis.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductRedisService productRedisService;

    public List<Product> getAllProducts(int page, int size) {
        List<Product> cachedProducts = productRedisService.getAllProducts(0, 10);
        if(cachedProducts != null) {
            return cachedProducts;
        }
        System.out.println("Fetching from database");
        Pageable pageable = PageRequest.of(page, size);
        cachedProducts = repository.findAll(pageable).getContent();
        productRedisService.cachedAllProducts(cachedProducts, page, size);
        return cachedProducts;
    }

    public Product getProductById(Long id) {
        Product cachedProduct = productRedisService.getProductById(id);
        if(cachedProduct != null) {
            return cachedProduct;
        }
        System.out.println("Fetching product with ID " + id + " from database");
        cachedProduct = repository.findById(id).orElse(null);
        productRedisService.cacheProduct(cachedProduct);
        return cachedProduct;
    }

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public Optional<Product> updateProduct(Long id, Product newProduct) {
        productRedisService.clearProduct(id);
        return repository.findById(id).map(product -> {
            product.setName(newProduct.getName());
            product.setPrice(newProduct.getPrice());
            product.setDescription(newProduct.getDescription());
            productRedisService.cacheProduct(product);
            System.out.println("Updating product with ID " + id + " in database");
            return repository.save(product);
        });
    }

    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }
}