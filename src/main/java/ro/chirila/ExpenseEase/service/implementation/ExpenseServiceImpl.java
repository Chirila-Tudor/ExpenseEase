package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.exception.ExpenseNotFoundException;
import ro.chirila.ExpenseEase.repository.ExpenseRepository;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.ExpenseRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.ExpenseResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.Expense;
import ro.chirila.ExpenseEase.repository.entity.Salary;
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
        Expense expense = new Expense();

        expense.setCategory(expenseRequestDTO.getCategory());
        expense.setAmount(expenseRequestDTO.getAmount());
        expense.setDate(expenseRequestDTO.getDate());

        User user = userRepository.findById(expenseRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + expenseRequestDTO.getUserId()));

        Salary salary = user.getSalary();
        if (salary == null) {
            throw new IllegalStateException("Salary not found for User ID: " + expenseRequestDTO.getUserId());
        }

        expense.setUser(user);
        expense.setSalary(salary);

        if (salary.getRemainingSalary() < expenseRequestDTO.getAmount()) {
            throw new IllegalArgumentException("Insufficient remaining salary!");
        }
        salary.setRemainingSalary(salary.getRemainingSalary() - expenseRequestDTO.getAmount());

        Expense savedExpense = expenseRepository.save(expense);

        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();
        responseDTO.setId(savedExpense.getId());
        responseDTO.setCategory(savedExpense.getCategory());
        responseDTO.setAmount(savedExpense.getAmount());
        responseDTO.setDate(savedExpense.getDate());
        responseDTO.setUserId(savedExpense.getUser().getId().toString());
        responseDTO.setSalaryId(savedExpense.getSalary().getId().toString());

        return responseDTO;
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

        Salary salary = expense.getSalary();
        if (salary == null) {
            throw new IllegalStateException("Salary not associated with Expense ID: " + id);
        }

        double oldAmount = expense.getAmount();
        double newAmount = expenseRequestDTO.getAmount();

        double adjustment = oldAmount - newAmount;
        if (salary.getRemainingSalary() + adjustment < 0) {
            throw new IllegalArgumentException("Insufficient remaining salary!");
        }
        salary.setRemainingSalary(salary.getRemainingSalary() + adjustment);

        expense.setCategory(expenseRequestDTO.getCategory());
        expense.setAmount(newAmount);
        expense.setDate(expenseRequestDTO.getDate());

        Expense updatedExpense = expenseRepository.save(expense);
        return modelMapper.map(updatedExpense, ExpenseResponseDTO.class);
    }

    @Override
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with ID: " + id));

        Salary salary = expense.getSalary();
        if (salary != null) {
            salary.setRemainingSalary(salary.getRemainingSalary() + expense.getAmount());
        }

        expenseRepository.delete(expense);
    }


}
