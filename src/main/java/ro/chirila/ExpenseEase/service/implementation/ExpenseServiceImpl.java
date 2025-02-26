package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.exception.ExpenseNotFoundException;
import ro.chirila.ExpenseEase.repository.ExpenseRepository;
import ro.chirila.ExpenseEase.repository.SalaryRepository;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.ExpenseRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.ExpenseResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.Expense;
import ro.chirila.ExpenseEase.repository.entity.Salary;
import ro.chirila.ExpenseEase.repository.entity.Transaction;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.ExpenseService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    public final SalaryRepository salaryRepository;
    private final ModelMapper modelMapper;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, SalaryRepository salaryRepository, ModelMapper modelMapper) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.salaryRepository = salaryRepository;
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

        Salary salary = salaryRepository.findById(expenseRequestDTO.getSalaryId())
                .orElseThrow(() -> new RuntimeException("Salary not found with id: " + expenseRequestDTO.getSalaryId()));

        if (salary.getRemainingSalary() < expenseRequestDTO.getAmount()) {
            throw new IllegalArgumentException("Insufficient remaining salary!");
        }
        expense.setUser(user);
        expense.setSalary(salary);


        salary.setRemainingSalary(salary.getRemainingSalary() - expenseRequestDTO.getAmount());

        expenseRepository.save(expense);
        salaryRepository.save(salary);

        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();
        responseDTO.setId(expense.getId());
        responseDTO.setCategory(expense.getCategory());
        responseDTO.setAmount(expense.getAmount());
        responseDTO.setDate(expense.getDate());
        responseDTO.setUserId(expense.getUser().getId().toString());
        responseDTO.setSalaryId(expense.getSalary().getId().toString());

        return responseDTO;
    }

    @Override
    public List<ExpenseResponseDTO> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(expense -> modelMapper.map(expense, ExpenseResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponseDTO getExpenseById(Long id) {
        Optional<Expense> expenseOptional = expenseRepository.findById(id);

        if (expenseOptional.isEmpty()) {
            throw new IllegalArgumentException("Salary not found with ID: " + id);
        }

        Expense expense = expenseOptional.get();
        return modelMapper.map(expense, ExpenseResponseDTO.class);
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
