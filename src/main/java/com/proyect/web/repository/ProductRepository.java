package com.proyect.web.repository;

import com.proyect.web.entitys.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByUserId(Long userId, Pageable pageable);
    Page<Product> findByCategoryProductCategoryId(Long categoryId, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.isSold = false")
    List<Product> findAvailableProducts();

    Page<Product> findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
            String name, String description, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "p.productId != :productId AND " +  // Excluir el producto actual
            "(" +
            "p.category.productCategoryId = :categoryId OR " +  // Misma categoría
            "LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%')) OR " +  // Nombre similar
            "LOWER(p.productDescription) LIKE LOWER(CONCAT('%', :description, '%'))" +  // Descripción similar
            ") " +
            "ORDER BY " +
            "CASE " +
            "   WHEN p.category.productCategoryId = :categoryId THEN 1 " +  // Priorizar misma categoría
            "   ELSE 2 " +
            "END")
    Page<Product> findRelatedProducts(
            @Param("productId") Long productId,
            @Param("categoryId") Long categoryId,
            @Param("productName") String productName,
            @Param("description") String description,
            Pageable pageable
    );
}
