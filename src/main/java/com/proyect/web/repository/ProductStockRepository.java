package com.proyect.web.repository;

import com.proyect.web.entitys.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    // Métodos básicos heredados de JpaRepository
}