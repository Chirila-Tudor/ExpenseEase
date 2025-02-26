package ro.chirila.ExpenseEase.service;

import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.ExpenseRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.ExpenseResponseDTO;

import java.util.List;
import java.util.Optional;

@Service
public interface ExpenseService {

    ExpenseResponseDTO addExpense(ExpenseRequestDTO expenseRequestDTO);
    List<ExpenseResponseDTO> getAllExpenses();
    ExpenseResponseDTO getExpenseById(Long id);
    ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO expenseRequestDTO);
    void deleteExpense(Long id);

}
