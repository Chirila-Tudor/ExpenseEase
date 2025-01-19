package ro.chirila.ExpenseEase.service;

import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.ExpenseRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.ExpenseResponseDTO;

@Service
public interface ExpenseService {

    ExpenseResponseDTO addExpense(ExpenseRequestDTO expenseRequestDTO);
}
