package com.proyect.web.service.Impl;

import com.proyect.web.dtos.transaction.TransactionCreateDTO;
import com.proyect.web.dtos.transaction.TransactionDTO;
import com.proyect.web.dtos.transaction.UserTransactionsDTO;
import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.entitys.Transaction;
import com.proyect.web.entitys.User;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.repository.ProductRepository;
import com.proyect.web.repository.TransactionRepository;
import com.proyect.web.repository.UserRepository;
import com.proyect.web.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TransactionCreateDTO createTransaction(TransactionCreateDTO transactionCreateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new InvalidOperationException("Usuario no autenticado");
        }
        String currentUserEmail = authentication.getName();

        User buyer = userRepository.findByUserNameOrEmail(currentUserEmail, currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", 0));

        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setProduct(productRepository.findById(transactionCreateDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", transactionCreateDTO.getProductId())));
        transaction.setTransactionDate(transactionCreateDTO.getTransactionDate());
        transaction.setStatus(transactionCreateDTO.getStatus());
        transaction.setPaymentMethod(transactionCreateDTO.getPaymentMethod());
        transaction = transactionRepository.save(transaction);
        return modelMapper.map(transaction, TransactionCreateDTO.class);
    }

    @Override
    public UserTransactionsDTO getUserTransactions(int buyerTransactionsPageNo, int buyerTransactionsPageSize, int sellerTransactionsPageNo, int sellerTransactionsPageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUserNameOrEmail(authentication.getName(), authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username/email", 0));

        Pageable buyerPageable = PageRequest.of(buyerTransactionsPageNo, buyerTransactionsPageSize);
        Page<Transaction> buyerTransactions = transactionRepository.findByBuyerId(currentUser.getId(), buyerPageable);

        Page<Transaction> sellerTransactions;
        if (currentUser.isSeller()) {
            Pageable sellerPageable = PageRequest.of(sellerTransactionsPageNo, sellerTransactionsPageSize);
            sellerTransactions = transactionRepository.findByProductUserId(currentUser.getId(), sellerPageable);
        } else {
            sellerTransactions = Page.empty();
        }

        UserDTO userDTO = modelMapper.map(currentUser, UserDTO.class);
        List<TransactionDTO> buyerTransactionDTOs = buyerTransactions.stream()
                .map(t -> {
                    TransactionDTO transactionDTO = modelMapper.map(t, TransactionDTO.class);
                    transactionDTO.setUserName(t.getProduct().getUser().getUserName());
                    transactionDTO.setSalePrice(t.getProduct().getSalePrice());
                    return transactionDTO;
                })
                .collect(Collectors.toList());
        List<TransactionDTO> sellerTransactionDTOs = sellerTransactions.stream()
                .map(t -> {
                    TransactionDTO transactionDTO = modelMapper.map(t, TransactionDTO.class);
                    transactionDTO.setUserName(t.getProduct().getUser().getUserName());
                    transactionDTO.setSalePrice(t.getProduct().getSalePrice());
                    return transactionDTO;
                })
                .collect(Collectors.toList());

        return new UserTransactionsDTO(
                userDTO,
                buyerTransactionDTOs,
                buyerTransactionsPageNo, buyerTransactionsPageSize, buyerTransactions.getTotalElements(), buyerTransactions.getTotalPages(), buyerTransactions.isLast(),
                sellerTransactionDTOs,
                sellerTransactionsPageNo, sellerTransactionsPageSize, sellerTransactions.getTotalElements(), sellerTransactions.getTotalPages(), sellerTransactions.isLast()
        );
    }
}
