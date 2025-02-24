package ro.chirila.ExpenseEase.service;

import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.SalaryRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.SalaryResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.SalaryUpdateRequestDTO;

import java.util.List;

@Service
public interface SalaryService {

    SalaryResponseDTO addSalary(SalaryRequestDTO salaryRequestDTO);
    SalaryResponseDTO updateSalary(Long salaryId, SalaryUpdateRequestDTO salaryUpdateRequestDTO);
    void deleteSalary(Long id);
    List<SalaryResponseDTO> getAllSalaries();
    SalaryResponseDTO getSalaryById(Long salaryId);
}
