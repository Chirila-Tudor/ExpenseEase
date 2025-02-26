package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.SalaryRepository;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.SalaryRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.SalaryResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.SalaryUpdateRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.Salary;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.SalaryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public SalaryServiceImpl(SalaryRepository salaryRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.salaryRepository = salaryRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SalaryResponseDTO addSalary(SalaryRequestDTO salaryRequestDTO) {
        Optional<User> userOptional = userRepository.findById(salaryRequestDTO.getUserId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + salaryRequestDTO.getUserId());
        }

        User user = userOptional.get();

        Salary salary = modelMapper.map(salaryRequestDTO, Salary.class);
        salary.setUser(user);
        salary.setRemainingSalary(salaryRequestDTO.getTotalSalary());
        salary.setDate(salaryRequestDTO.getDate());

        Salary savedSalary = salaryRepository.save(salary);

        return modelMapper.map(savedSalary, SalaryResponseDTO.class);
    }

    @Override
    public SalaryResponseDTO updateSalary(Long salaryId, SalaryUpdateRequestDTO salaryUpdateRequestDTO) {
        Optional<User> userOptional = userRepository.findById(salaryUpdateRequestDTO.getUserId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + salaryUpdateRequestDTO.getUserId());
        }

        User user = userOptional.get();

        Optional<Salary> salaryOptional = salaryRepository.findById(salaryId);
        if (salaryOptional.isEmpty()) {
            throw new IllegalArgumentException("Salary not found with ID: " + salaryId);
        }

        Salary existingSalary = salaryOptional.get();
        if (!existingSalary.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Salary does not belong to the provided user ID");
        }

        double previousRemainingSalary = existingSalary.getRemainingSalary();
        double previousTotalSalary = existingSalary.getTotalSalary();

        double adjustedRemainingSalary = previousRemainingSalary + (salaryUpdateRequestDTO.getTotalSalary() - previousTotalSalary);

        existingSalary.setTotalSalary(salaryUpdateRequestDTO.getTotalSalary());
        existingSalary.setRemainingSalary(adjustedRemainingSalary);
        existingSalary.setDate(salaryUpdateRequestDTO.getDate());

        Salary savedSalary = salaryRepository.save(existingSalary);

        return modelMapper.map(savedSalary, SalaryResponseDTO.class);
    }

    @Override
    public void deleteSalary(Long id) {
        Optional<Salary> salaryOptional = salaryRepository.findById(id);
        if (salaryOptional.isEmpty()) {
            throw new IllegalArgumentException("Salary not found with ID: " + id);
        }

        Salary salary = salaryOptional.get();

        User user = salary.getUser();
        if (user != null) {
            user.setSalary(null);
            userRepository.save(user);
        }
        salaryRepository.delete(salary);
    }

    @Override
    public List<SalaryResponseDTO> getAllSalaries() {
        return salaryRepository.findAll().stream()
                .map(salaries -> modelMapper.map(salaries, SalaryResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SalaryResponseDTO getSalaryById(Long salaryId) {
        Optional<Salary> salaryOptional = salaryRepository.findById(salaryId);

        if (salaryOptional.isEmpty()) {
            throw new IllegalArgumentException("Salary not found with ID: " + salaryId);
        }

        Salary salary = salaryOptional.get();
        return modelMapper.map(salary, SalaryResponseDTO.class);
    }


}
