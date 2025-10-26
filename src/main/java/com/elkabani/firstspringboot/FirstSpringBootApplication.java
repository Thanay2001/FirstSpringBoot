package com.elkabani.firstspringboot;

import com.elkabani.firstspringboot.model.Category;
import com.elkabani.firstspringboot.model.Product;
import com.elkabani.firstspringboot.repository.CategoryRepository;
import com.elkabani.firstspringboot.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootApplication
public class FirstSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstSpringBootApplication.class, args);
    }

    /**
     * Idempotent data seeder for local testing.
     * Runs ONLY when spring.profiles.active includes "devseed".
     */
    @Bean
    @Profile("devseed")
    CommandLineRunner seedData(CategoryRepository categoryRepo, ProductRepository productRepo) {
        return args -> {
            System.out.println("=== SEED START ===");

            // Ensure categories exist (no duplicates)
            Category electronics = categoryRepo.findByName("Electronics")
                    .orElseGet(() -> categoryRepo.save(new Category("Electronics")));

            Category pets = categoryRepo.findByName("Pets")
                    .orElseGet(() -> categoryRepo.save(new Category("Pets")));

            // Helper to upsert product by name
            upsertProduct(productRepo, "Camera", "4K mirrorless camera",
                    new BigDecimal("399.99"), electronics);

            upsertProduct(productRepo, "Headphones", "Wireless noise-cancelling",
                    new BigDecimal("99.95"), electronics);

            upsertProduct(productRepo, "Cat Toy", "Feather teaser",
                    new BigDecimal("9.99"), pets);

            upsertProduct(productRepo, "Dog Bed", "Memory foam medium size",
                    new BigDecimal("49.99"), pets);

            System.out.println("=== CURRENT PRODUCTS ===");
            productRepo.findAll().forEach(p ->
                    System.out.printf(" - [%d] %s | %s | cat=%s%n",
                            p.getId(), p.getName(), p.getPrice(), p.getCategory().getName())
            );

            System.out.println("=== SEED END ===");
        };
    }

    private static void upsertProduct(
            ProductRepository repo, String name, String desc, BigDecimal price, Category category) {

        Optional<Product> existing = repo.findByName(name) == null
                ? Optional.empty()
                : repo.findAll().stream().filter(p -> p.getName().equals(name)).findFirst();

        if (existing.isPresent()) {
            Product p = existing.get();
            // update fields in case they were blank
            p.setDescription(desc);
            p.setPrice(price);
            p.setCategory(category);
            repo.save(p);
        } else {
            // adapt to your constructor; this assumes (name, description, price, category)
            Product p = new Product();
            p.setName(name);
            p.setDescription(desc);
            p.setPrice(price);
            p.setCategory(category);
            repo.save(p);
        }
    }
}
