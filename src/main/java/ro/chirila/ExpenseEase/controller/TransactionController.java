package ro.chirila.ExpenseEase.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.ExpenseEase.repository.dto.TransactionRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionResponseDTO;
import ro.chirila.ExpenseEase.service.TransactionService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Transactional
    @PostMapping("/addTransaction")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO transactionResponseDTO = transactionService.addTransaction(transactionRequestDTO);
        return ResponseEntity.ok(transactionResponseDTO);
    }
    @PutMapping("/updateTransaction")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(@RequestParam Long id, @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return new ResponseEntity<>(transactionService.updateTransaction(id, transactionRequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/deleteTransaction")
    public ResponseEntity<Void> deleteTransaction(@RequestParam Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }
}
