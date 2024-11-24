package com.proyect.web.service.Impl;

import com.proyect.web.dtos.transaction.TransactionCreateDTO;
import com.proyect.web.dtos.transaction.TransactionDTO;
import com.proyect.web.dtos.transaction.TransactionItemDTO;
import com.proyect.web.dtos.transaction.UserTransactionsDTO;
import com.proyect.web.dtos.user.UserResponseDTO;
import com.proyect.web.entitys.*;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.repository.ProductRepository;
import com.proyect.web.repository.ProductStockRepository;
import com.proyect.web.repository.TransactionRepository;
import com.proyect.web.repository.UserRepository;
import com.proyect.web.responses.ApiResponse;
import com.proyect.web.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            ProductRepository productRepository,
            ProductStockRepository productStockRepository,
            UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.productStockRepository = productStockRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ApiResponse<Void> createTransaction(TransactionCreateDTO createDTO) {
        try {
            // Obtener usuario autenticado
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User buyer = userRepository.findByUserNameOrEmail(currentUserEmail, currentUserEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", 0));

            // Validar disponibilidad de stock para todos los productos
            validateProductsStock(createDTO.getItems());

            // Procesar cada item
            for (TransactionItemDTO item : createDTO.getItems()) {
                processTransactionItem(item, buyer, createDTO.getPaymentMethod());
            }

            return new ApiResponse<>("Compra realizada exitosamente", null, true);

        } catch (ResourceNotFoundException | InvalidOperationException e) {
            return new ApiResponse<>(e.getMessage(), null, false);
        } catch (Exception e) {
            return new ApiResponse<>("Error al procesar la compra: " + e.getMessage(), null, false);
        }
    }

    private void validateProductsStock(List<TransactionItemDTO> items) {
        for (TransactionItemDTO item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", item.getProductId()));

            if (product.getIsSold()) {
                throw new InvalidOperationException(
                        "El producto " + product.getProductName() + " ya no está disponible"
                );
            }

            if (product.getStock().getQuantity() < item.getQuantity()) {
                throw new InvalidOperationException(
                        String.format("Stock insuficiente para el producto %s. Disponible: %d, Solicitado: %d",
                                product.getProductName(),
                                product.getStock().getQuantity(),
                                item.getQuantity())
                );
            }
        }
    }

    private void processTransactionItem(
            TransactionItemDTO item,
            User buyer,
            PaymentMethod paymentMethod) {

        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", item.getProductId()));

        // Actualizar stock
        ProductStock stock = product.getStock();
        int newQuantity = stock.getQuantity() - item.getQuantity();
        stock.setQuantity(newQuantity);
        productStockRepository.save(stock);

        // Si el stock llega a 0, marcar el producto como vendido
        if (newQuantity == 0) {
            product.setIsSold(true);
            productRepository.save(product);
        }

        // Crear la transacción
        Transaction transaction = new Transaction();
        transaction.setBuyer(buyer);
        transaction.setProduct(product);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setPaymentMethod(paymentMethod);

        transactionRepository.save(transaction);
    }

    @Override
    public ApiResponse<UserTransactionsDTO> getUserTransactions(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir) {
        try {
            // Obtener usuario actual
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userRepository.findByUserNameOrEmail(currentUserEmail, currentUserEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", 0));

            // Configurar paginación
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                    Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

            // Obtener compras
            Page<Transaction> buyerTransactionsPage = transactionRepository
                    .findByBuyerId(currentUser.getId(), pageable);

            // Obtener ventas
            Page<Transaction> sellerTransactionsPage = transactionRepository
                    .findByProductSellerId(currentUser.getId(), pageable);

            // Construir respuesta
            UserTransactionsDTO userTransactions = new UserTransactionsDTO();

            // Información del usuario
            userTransactions.setUserResponseDTO(convertToUserResponseDTO(currentUser));

            // Información de compras
            userTransactions.setBuyerTransactions(
                    buyerTransactionsPage.getContent().stream()
                            .map(this::convertToTransactionDTO)
                            .collect(Collectors.toList()));
            userTransactions.setBuyerTransactionsPageNo(buyerTransactionsPage.getNumber());
            userTransactions.setBuyerTransactionsPageSize(buyerTransactionsPage.getSize());
            userTransactions.setBuyerTransactionsTotalElements(buyerTransactionsPage.getTotalElements());
            userTransactions.setBuyerTransactionsTotalPages(buyerTransactionsPage.getTotalPages());
            userTransactions.setBuyerTransactionsLast(buyerTransactionsPage.isLast());

            // Información de ventas
            userTransactions.setSellerTransactions(
                    sellerTransactionsPage.getContent().stream()
                            .map(this::convertToTransactionDTO)
                            .collect(Collectors.toList()));
            userTransactions.setSellerTransactionsPageNo(sellerTransactionsPage.getNumber());
            userTransactions.setSellerTransactionsPageSize(sellerTransactionsPage.getSize());
            userTransactions.setSellerTransactionsTotalElements(sellerTransactionsPage.getTotalElements());
            userTransactions.setSellerTransactionsTotalPages(sellerTransactionsPage.getTotalPages());
            userTransactions.setSellerTransactionsLast(sellerTransactionsPage.isLast());

            return new ApiResponse<>("Transacciones recuperadas exitosamente",
                    userTransactions, true);

        } catch (Exception e) {
            return new ApiResponse<>(e.getMessage(), null, false);
        }
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserImage(user.getUserImage());
        dto.setSeller(user.isSeller());
        return dto;
    }

    private TransactionDTO convertToTransactionDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getTransactionId());
        dto.setBuyerId(transaction.getBuyer().getId());
        dto.setProductId(transaction.getProduct().getProductId());
        dto.setImageUrls(transaction.getProduct().getImages().stream()
                .map(ProductImage::getUrl)
                .collect(Collectors.toList()));
        dto.setProductName(transaction.getProduct().getProductName());
        dto.setUserName(transaction.getBuyer().getUserName());
        dto.setSalePrice(transaction.getProduct().getSalePrice());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setStatus(transaction.getStatus());
        dto.setPaymentMethod(transaction.getPaymentMethod());
        return dto;
    }
}
