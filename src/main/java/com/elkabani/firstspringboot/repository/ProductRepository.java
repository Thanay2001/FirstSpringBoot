package com.elkabani.firstspringboot.repository;

import com.elkabani.firstspringboot.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
