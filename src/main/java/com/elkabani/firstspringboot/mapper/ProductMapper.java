package com.elkabani.firstspringboot.mapper;

import com.elkabani.firstspringboot.dto.ProductDto;
import com.elkabani.firstspringboot.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product p) {
        if (p == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setCategoryId(Long.valueOf(p.getCategory() != null ? p.getCategory().getId() : null));
        return dto;
    }

    public Product toEntity(ProductDto dto) {
        if (dto == null) return null;
        Product p = new Product();
        // id ignored on create; service sets Category
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        return p;
    }

    public void update(ProductDto dto, Product target) {
        if (dto == null || target == null) return;
        if (dto.getName() != null) target.setName(dto.getName());
        if (dto.getDescription() != null) target.setDescription(dto.getDescription());
        if (dto.getPrice() != null) target.setPrice(dto.getPrice());
        // category is set in service when dto.getCategoryId() != null
    }
}
