package com.proyect.web.repository;

import com.proyect.web.dtos.productImage.ProductImageDTO;
import com.proyect.web.entitys.ProductImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductProductId(Long productId);
    void deleteByProductProductId(Long productId);
    boolean existsByUrl(String url);
}
