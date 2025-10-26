package com.elkabani.firstspringboot.service;

import com.elkabani.firstspringboot.dto.ProductDto;
import com.elkabani.firstspringboot.mapper.ProductMapper;
import com.elkabani.firstspringboot.model.Category;
import com.elkabani.firstspringboot.model.Product;
import com.elkabani.firstspringboot.repository.CategoryRepository;
import com.elkabani.firstspringboot.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts(Long categoryId) {
        List<Product> products = (categoryId == null)
                ? productRepository.findAll()
                : productRepository.findAllByCategory_Id(categoryId);
        return products.stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ProductDto getById(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product %d not found".formatted(id)));
        return mapper.toDto(p);
    }

    @Transactional
    public ProductDto create(ProductDto dto) {
        Category cat = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category %d not found".formatted(dto.getCategoryId())));
        Product entity = mapper.toEntity(dto);
        entity.setCategory(cat);
        return mapper.toDto(productRepository.save(entity));
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product %d not found".formatted(id)));

        mapper.update(dto, existing);

        if (dto.getCategoryId() != null) {
            Category cat = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category %d not found".formatted(dto.getCategoryId())));
            existing.setCategory(cat);
        }
        return mapper.toDto(productRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product %d not found".formatted(id));
        }
        productRepository.deleteById(id);
    }
}
