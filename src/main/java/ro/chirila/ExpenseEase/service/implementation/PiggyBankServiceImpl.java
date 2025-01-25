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
import ro.chirila.ExpenseEase.repository.entity.PiggyBank;
import ro.chirila.ExpenseEase.repository.entity.Salary;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.PiggyBankService;

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

        PiggyBank piggyBank = new PiggyBank();
        piggyBank.setAmount(piggyBankRequestDTO.getAmount());
        piggyBank.setDate(piggyBankRequestDTO.getDate());

        User user = userRepository.findById(piggyBankRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + piggyBankRequestDTO.getUserId()));

        Salary salary = user.getSalary();
        if (salary == null) {
            throw new IllegalStateException("Salary not found for User ID: " + piggyBankRequestDTO.getUserId());
        }

        piggyBank.setUser(user);
        piggyBank.setSalary(salary);

        salary.setRemainingSalary(salary.getRemainingSalary() - piggyBankRequestDTO.getAmount());

        piggyBankRepository.save(piggyBank);
        salaryRepository.save(salary);

        PiggyBankResponseDTO responseDTO = new PiggyBankResponseDTO();
        responseDTO.setId(piggyBank.getId());
        responseDTO.setAmount(piggyBank.getAmount());
        responseDTO.setDate(piggyBank.getDate());
        responseDTO.setUserId(piggyBank.getUser().getId().toString());
        responseDTO.setSalaryId(piggyBank.getSalary().getId().toString());

        return responseDTO;
    }

    @Override
    public PiggyBankResponseDTO updatePiggyBankAmount(Long id, PiggyBankUpdateRequestDTO piggyBankUpdateRequestDTO) {
        PiggyBank piggyBank = piggyBankRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + id));

        Salary salary = piggyBank.getSalary();
        if (salary == null) {
            throw new IllegalStateException("Salary not associated with Transaction ID: " + id);
        }

        double oldAmount = piggyBank.getAmount();
        double newAmount = piggyBankUpdateRequestDTO.getAmount();
        double adjustment = oldAmount - newAmount;

        if (salary.getRemainingSalary() + adjustment < 0) {
            throw new IllegalArgumentException("Insufficient remaining salary!");
        }

        salary.setRemainingSalary(salary.getRemainingSalary() + adjustment);

        piggyBank.setAmount(newAmount);

        PiggyBank updatedPiggyBank = piggyBankRepository.save(piggyBank);

        return modelMapper.map(updatedPiggyBank, PiggyBankResponseDTO.class);
    }

    @Override
    public void deleteSaving(Long id) {
        PiggyBank piggyBank = piggyBankRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Piggy Bank not found with ID: " + id));

        Salary salary = piggyBank.getSalary();
        if (salary != null) {
            salary.setRemainingSalary(salary.getRemainingSalary() + piggyBank.getAmount());
        }

        piggyBankRepository.delete(piggyBank);
    }
}
