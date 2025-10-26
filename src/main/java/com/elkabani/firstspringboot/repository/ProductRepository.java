package com.elkabani.firstspringboot.repository;

import com.elkabani.firstspringboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String name);

    // <-- This is the key for filtering by categoryId
    List<Product> findAllByCategory_Id(Long categoryId);
}
