package com.proyect.web.service;

import com.proyect.web.dtos.transaction.TransactionCreateDTO;
import com.proyect.web.dtos.transaction.TransactionResponseDTO;
import com.proyect.web.dtos.transaction.UserTransactionsDTO;
import com.proyect.web.entitys.Transaction;
import com.proyect.web.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionService {
    ApiResponse<Void> createTransaction(TransactionCreateDTO createDTO);
    ApiResponse<UserTransactionsDTO> getUserTransactions(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir);
}
