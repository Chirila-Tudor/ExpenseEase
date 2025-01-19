package ro.chirila.ExpenseEase.service;

import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.TransactionRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionResponseDTO;

@Service
public interface TransactionService {

    TransactionResponseDTO addTransaction(TransactionRequestDTO transactionRequestDTO);
}
