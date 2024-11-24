package com.proyect.web.service;

import com.proyect.web.dtos.transaction.TransactionCreateDTO;
import com.proyect.web.dtos.transaction.UserTransactionsDTO;
import com.proyect.web.entitys.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionCreateDTO createTransaction(TransactionCreateDTO transactionCreateDTO);
    UserTransactionsDTO getUserTransactions(int buyerTransactionsPageNo, int buyerTransactionsPageSize, int sellerTransactionsPageNo, int sellerTransactionsPageSize);
}
