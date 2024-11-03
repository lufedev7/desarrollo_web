package com.proyect.web.repository;

import com.proyect.web.entitys.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserId(Long userId);
    List<Product> findByCategoryProductCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.isSold = false")
    List<Product> findAvailableProducts();
}
