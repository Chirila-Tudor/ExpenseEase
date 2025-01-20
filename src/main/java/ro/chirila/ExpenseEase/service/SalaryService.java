package ro.chirila.ExpenseEase.service;

import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.SalaryRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.SalaryResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.SalaryUpdateRequestDTO;

@Service
public interface SalaryService {

    SalaryResponseDTO addSalary(SalaryRequestDTO salaryRequestDTO);
    SalaryResponseDTO updateSalary(Long salaryId, SalaryUpdateRequestDTO salaryUpdateRequestDTO);
    void deleteSalary(Long id);
}
