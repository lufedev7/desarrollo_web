package com.proyect.web.controllers;

import com.proyect.web.dtos.transaction.TransactionCreateDTO;
import com.proyect.web.dtos.transaction.UserTransactionsDTO;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionCreateDTO> createTransaction(@RequestBody TransactionCreateDTO transactionCreateDTO) {
        TransactionCreateDTO createdTransaction = transactionService.createTransaction(transactionCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @GetMapping("/user")
    public ResponseEntity<UserTransactionsDTO> getUserTransactions(
            @RequestParam(defaultValue = "0") int buyerTransactionsPageNo,
            @RequestParam(defaultValue = "10") int buyerTransactionsPageSize,
            @RequestParam(defaultValue = "0") int sellerTransactionsPageNo,
            @RequestParam(defaultValue = "10") int sellerTransactionsPageSize) {
        try {
            UserTransactionsDTO userTransactionsDTO = transactionService.getUserTransactions(buyerTransactionsPageNo, buyerTransactionsPageSize, sellerTransactionsPageNo, sellerTransactionsPageSize);
            return ResponseEntity.ok(userTransactionsDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserTransactionsDTO(null, null, 0, 0, 0, 0, true, null, 0, 0, 0, 0, true));
        }
    }
}
