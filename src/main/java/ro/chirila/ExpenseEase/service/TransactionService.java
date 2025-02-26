package ro.chirila.ExpenseEase.service;

import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.SalaryResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionResponseDTO;

import java.util.List;

@Service
public interface TransactionService {

    TransactionResponseDTO addTransaction(TransactionRequestDTO transactionRequestDTO);
    TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO transactionRequestDTO);
    void deleteTransaction(Long id);
    List<TransactionResponseDTO> getAllTransactions();
    TransactionResponseDTO getTransactionById(Long transactionId);
}
