package com.proyect.web.dtos.transaction;
import com.proyect.web.entitys.PaymentMethod;
import com.proyect.web.entitys.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private Long buyerId;
    private Long productId;
    private List<String> imageUrls;
    private String productName;
    private String userName;
    private BigDecimal salePrice;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
    private PaymentMethod paymentMethod;
}
