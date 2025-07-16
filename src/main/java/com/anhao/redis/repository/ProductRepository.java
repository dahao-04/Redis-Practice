package com.anhao.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anhao.redis.model.Product;

@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {

}
