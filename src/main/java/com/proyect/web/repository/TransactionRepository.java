package com.proyect.web.repository;

import com.proyect.web.entitys.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    Page<Transaction> findByBuyerId(Long buyerId, Pageable pageable);
    Page<Transaction> findByProductUserId(Long sellerId, Pageable pageable);
}
