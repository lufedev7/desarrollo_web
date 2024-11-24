package com.proyect.web.dtos.transaction;
import com.proyect.web.entitys.PaymentMethod;
import com.proyect.web.entitys.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private Long buyerId;
    private Long productId;
    private String userName;
    private BigDecimal salePrice;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
    private PaymentMethod paymentMethod;
}
