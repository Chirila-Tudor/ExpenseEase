package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.SalaryRepository;
import ro.chirila.ExpenseEase.repository.TransactionRepository;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.SalaryResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.Salary;
import ro.chirila.ExpenseEase.repository.entity.Transaction;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.TransactionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    public final TransactionRepository transactionRepository;
    public final UserRepository userRepository;
    public final SalaryRepository salaryRepository;
    public final ModelMapper modelMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, SalaryRepository salaryRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.salaryRepository = salaryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TransactionResponseDTO addTransaction(TransactionRequestDTO transactionRequestDTO) {

        Transaction transaction = new Transaction();
        transaction.setDescription(transactionRequestDTO.getDescription());
        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setDate(transactionRequestDTO.getDate());

        User user = userRepository.findById(transactionRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + transactionRequestDTO.getUserId()));

        Salary salary = salaryRepository.findById(transactionRequestDTO.getSalaryId())
                .orElseThrow(() -> new RuntimeException("Salary not found with id: " + transactionRequestDTO.getSalaryId()));

        if (transactionRequestDTO.getAmount() > salary.getRemainingSalary()) {
            throw new IllegalArgumentException("Transaction amount exceeds the remaining salary.");
        }

        transaction.setUser(user);
        transaction.setSalary(salary);

        salary.setRemainingSalary(salary.getRemainingSalary() - transactionRequestDTO.getAmount());

        transactionRepository.save(transaction);
        salaryRepository.save(salary);

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setId(transaction.getId());
        responseDTO.setDescription(transaction.getDescription());
        responseDTO.setAmount(transaction.getAmount());
        responseDTO.setDate(transaction.getDate());
        responseDTO.setUserId(transaction.getUser().getId().toString());
        responseDTO.setSalaryId(transaction.getSalary().getId().toString());

        return responseDTO;
    }

    @Override
    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + id));

        Salary salary = transaction.getSalary();
        if (salary == null) {
            throw new IllegalStateException("Salary not associated with Transaction ID: " + id);
        }

        double oldAmount = transaction.getAmount();
        double newAmount = transactionRequestDTO.getAmount();
        double adjustment = oldAmount - newAmount;

        if (salary.getRemainingSalary() + adjustment < 0) {
            throw new IllegalArgumentException("Insufficient remaining salary!");
        }

        salary.setRemainingSalary(salary.getRemainingSalary() + adjustment);

        transaction.setDescription(transactionRequestDTO.getDescription());
        transaction.setAmount(newAmount);
        transaction.setDate(transactionRequestDTO.getDate());

        Transaction updatedTransaction = transactionRepository.save(transaction);

        return modelMapper.map(updatedTransaction, TransactionResponseDTO.class);
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + id));

        Salary salary = transaction.getSalary();
        if (salary != null) {
            salary.setRemainingSalary(salary.getRemainingSalary() + transaction.getAmount());
        }

        transactionRepository.delete(transaction);
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactions -> modelMapper.map(transactions, TransactionResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponseDTO getTransactionById(Long transactionId) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);

        if (transactionOptional.isEmpty()) {
            throw new IllegalArgumentException("Salary not found with ID: " + transactionId);
        }

        Transaction transaction = transactionOptional.get();
        return modelMapper.map(transaction, TransactionResponseDTO.class);
    }


}
