package com.anhao.redis.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.anhao.redis.model.Product;

@Service
public class ProductRedisService {

    @Autowired
    private RedisTemplate<String, Object> productRedisTemplate;

    public void clearAllProduct(int page, int size) {
        String key = "products:page:" + page + ":size:" + size;
        productRedisTemplate.delete(key);
    };
    public void cachedAllProducts(List<Product> products, int page, int size) {
        ValueOperations<String, Object> valueOperations = productRedisTemplate.opsForValue();
        String key = "products:page:" + page + ":size:" + size;
        valueOperations.set(key, products, Duration.ofMinutes(10));
    }
    @SuppressWarnings("unchecked")
    public List<Product> getAllProducts(int page, int size) {
        ValueOperations<String, Object> valueOperations = productRedisTemplate.opsForValue();
        String key = "products:page:" + page + ":size:" + size;
        Object cached = valueOperations.get(key);
        if(cached != null) {
            return (List<Product>) cached;
        }
        return null;
    };

    public Product getProductById (Long id) {
        ValueOperations<String, Object> ops = productRedisTemplate.opsForValue();
        String key = "product:" + id;
        Product cached = (Product) ops.get(key);
        if(cached != null) {
            return cached;
        }
        return null;
    }

    public void cacheProduct(Product product) {
        ValueOperations<String, Object> ops = productRedisTemplate.opsForValue();
        String key = "product:" + product.getId();
        ops.set(key, product, Duration.ofMinutes(10));
    }

    public void clearProduct(Long id) {
        String key = "product:" + id;
        productRedisTemplate.delete(key);
    }

}
