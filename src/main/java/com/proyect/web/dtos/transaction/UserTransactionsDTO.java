package com.proyect.web.dtos.transaction;

import com.proyect.web.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionsDTO {
    private UserDTO user;
    private List<TransactionDTO> buyerTransactions;
    private int buyerTransactionsPageNo;
    private int buyerTransactionsPageSize;
    private long buyerTransactionsTotalElements;
    private int buyerTransactionsTotalPages;
    private boolean buyerTransactionsLast;
    private List<TransactionDTO> sellerTransactions;
    private int sellerTransactionsPageNo;
    private int sellerTransactionsPageSize;
    private long sellerTransactionsTotalElements;
    private int sellerTransactionsTotalPages;
    private boolean sellerTransactionsLast;
}
