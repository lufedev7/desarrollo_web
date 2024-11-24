package com.proyect.web.repository;

import com.proyect.web.entitys.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    // Buscar compras del usuario
    @Query("SELECT t FROM Transaction t WHERE t.buyer.id = :userId")
    Page<Transaction> findByBuyerId(@Param("userId") Long userId, Pageable pageable);

    // Buscar ventas del usuario (productos que él vendió)
    @Query("SELECT t FROM Transaction t WHERE t.product.user.id = :userId")
    Page<Transaction> findByProductSellerId(@Param("userId") Long userId, Pageable pageable);


}
