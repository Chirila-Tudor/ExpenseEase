package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.ExpenseRepository;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.ExpenseRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.ExpenseResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.Expense;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ExpenseResponseDTO addExpense(ExpenseRequestDTO expenseRequestDTO) {
        User user = userRepository.findById(expenseRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + expenseRequestDTO.getUserId()));

        Expense expense = modelMapper.map(expenseRequestDTO, Expense.class);
        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);
        return modelMapper.map(savedExpense, ExpenseResponseDTO.class);
    }
}
