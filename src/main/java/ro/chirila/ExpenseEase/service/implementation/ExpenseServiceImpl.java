package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.exception.ExpenseNotFoundException;
import ro.chirila.ExpenseEase.repository.ExpenseRepository;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.ExpenseRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.ExpenseResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.Expense;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.ExpenseService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<ExpenseResponseDTO> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(expense -> modelMapper.map(expense, ExpenseResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ExpenseResponseDTO> getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .map(expense -> modelMapper.map(expense, ExpenseResponseDTO.class));
    }

    @Override
    public ExpenseResponseDTO updateExpense(Long id, ExpenseRequestDTO expenseRequestDTO) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with ID: " + id));

        User user = userRepository.findById(expenseRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + expenseRequestDTO.getUserId()));

        expense.setCategory(expenseRequestDTO.getCategory());
        expense.setAmount(expenseRequestDTO.getAmount());
        expense.setDate(expenseRequestDTO.getDate());
        expense.setUser(user);

        Expense updatedExpense = expenseRepository.save(expense);
        return modelMapper.map(updatedExpense, ExpenseResponseDTO.class);
    }

    @Override
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with ID: " + id));
        expenseRepository.delete(expense);
    }


}
