package com.proyect.web.dtos.transaction;

import com.proyect.web.entitys.PaymentMethod;
import com.proyect.web.entitys.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateDTO {
    private Long id;
    private Long buyerId;
    private Long productId;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
    private PaymentMethod paymentMethod;
}
