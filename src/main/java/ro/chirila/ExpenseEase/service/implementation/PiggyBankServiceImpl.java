package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.PiggyBankRepository;
import ro.chirila.ExpenseEase.repository.SalaryRepository;
import ro.chirila.ExpenseEase.repository.TransactionRepository;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.PiggyBankRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.PiggyBankResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.PiggyBankUpdateRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.PiggyBank;
import ro.chirila.ExpenseEase.repository.entity.Salary;
import ro.chirila.ExpenseEase.repository.entity.Transaction;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.PiggyBankService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PiggyBankServiceImpl implements PiggyBankService {

    public final TransactionRepository transactionRepository;
    public final UserRepository userRepository;
    public final SalaryRepository salaryRepository;
    public final ModelMapper modelMapper;
    private final PiggyBankRepository piggyBankRepository;

    public PiggyBankServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, SalaryRepository salaryRepository, ModelMapper modelMapper, PiggyBankRepository piggyBankRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.salaryRepository = salaryRepository;
        this.modelMapper = modelMapper;
        this.piggyBankRepository = piggyBankRepository;
    }

    @Override
    public PiggyBankResponseDTO addMoneyIntoPiggyBank(PiggyBankRequestDTO piggyBankRequestDTO) {

        User user = userRepository.findById(piggyBankRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + piggyBankRequestDTO.getUserId()));

        Salary salary = user.getSalary();
        if (salary == null) {
            throw new IllegalStateException("Salary not found for User ID: " + piggyBankRequestDTO.getUserId());
        }

        if (salary.getRemainingSalary() < piggyBankRequestDTO.getAmount()) {
            throw new IllegalArgumentException("Insufficient remaining salary!");
        }

        PiggyBank piggyBank = new PiggyBank();
        piggyBank.setAmount(piggyBankRequestDTO.getAmount());
        piggyBank.setDate(piggyBankRequestDTO.getDate());
        piggyBank.setUser(user);
        piggyBank.setSalary(salary);

        salary.setRemainingSalary(salary.getRemainingSalary() - piggyBankRequestDTO.getAmount());

        piggyBankRepository.save(piggyBank);
        salaryRepository.save(salary);

        return modelMapper.map(piggyBank, PiggyBankResponseDTO.class);
    }

    @Override
    public PiggyBankResponseDTO updatePiggyBankAmount(Long id, PiggyBankUpdateRequestDTO piggyBankUpdateRequestDTO) {
        PiggyBank piggyBank = piggyBankRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Piggy Bank not found with ID: " + id));

        Salary salary = piggyBank.getSalary();
        if (salary == null) {
            throw new IllegalStateException("Salary not associated with Piggy Bank ID: " + id);
        }

        double oldAmount = piggyBank.getAmount();
        double newAmount = piggyBankUpdateRequestDTO.getAmount();
        double adjustment = newAmount - oldAmount;

        if (salary.getRemainingSalary() - adjustment < 0) {
            throw new IllegalArgumentException("Insufficient remaining salary!");
        }

        salary.setRemainingSalary(salary.getRemainingSalary() - adjustment);
        piggyBank.setAmount(newAmount);

        piggyBankRepository.save(piggyBank);
        salaryRepository.save(salary);

        return modelMapper.map(piggyBank, PiggyBankResponseDTO.class);
    }

    @Override
    public void deleteSaving(Long id) {
        PiggyBank piggyBank = piggyBankRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Piggy Bank not found with ID: " + id));

        Salary salary = piggyBank.getSalary();
        if (salary != null) {
            salary.setRemainingSalary(salary.getRemainingSalary() + piggyBank.getAmount());
            salaryRepository.save(salary);
        }

        piggyBankRepository.delete(piggyBank);
    }

    @Override
    public List<PiggyBankResponseDTO> getAllSavings() {
        return piggyBankRepository.findAll().stream()
                .map(savings -> modelMapper.map(savings, PiggyBankResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PiggyBankResponseDTO getSavingById(Long savingId) {
        Optional<PiggyBank> savingOptional = piggyBankRepository.findById(savingId);

        if (savingOptional.isEmpty()) {
            throw new IllegalArgumentException("Salary not found with ID: " + savingId);
        }

        PiggyBank piggyBank = savingOptional.get();
        return modelMapper.map(piggyBank, PiggyBankResponseDTO.class);
    }

    @Override
    public double getTotalSavingsAmount() {
        return piggyBankRepository.findAll().stream()
                .mapToDouble(PiggyBank::getAmount)
                .sum();
    }
}
