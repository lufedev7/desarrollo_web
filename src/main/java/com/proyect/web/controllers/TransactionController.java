package com.proyect.web.controllers;

import com.proyect.web.dtos.transaction.TransactionCreateDTO;
import com.proyect.web.dtos.transaction.UserTransactionsDTO;
import com.proyect.web.responses.ApiResponse;
import com.proyect.web.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transacciones", description = "API para gestionar transacciones de compra")
@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Crear nueva transacción de compra",
            description = "Procesa una compra de uno o más productos, actualizando stock y creando registros de transacción")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Compra procesada exitosamente")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Error en la solicitud o stock insuficiente")
    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse<Void>> createTransaction(
            @Valid @RequestBody TransactionCreateDTO createDTO) {
        ApiResponse<Void> response = transactionService.createTransaction(createDTO);

        return response.getSuccess()
                ? new ResponseEntity<>(response, HttpStatus.CREATED)
                : new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/history")
    @Operation(summary = "Obtener historial de transacciones del usuario")
    public ResponseEntity<ApiResponse<UserTransactionsDTO>> getUserTransactions(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        ApiResponse<UserTransactionsDTO> response = transactionService
                .getUserTransactions(pageNumber, pageSize, sortBy, sortDir);

        return response.getSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }
}