package ro.chirila.ExpenseEase.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.ExpenseEase.repository.dto.SalaryRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.SalaryResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.SalaryUpdateRequestDTO;
import ro.chirila.ExpenseEase.service.SalaryService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/salaries")
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @Transactional
    @PostMapping("/addSalary")
    public ResponseEntity<SalaryResponseDTO> addSalary(@RequestBody SalaryRequestDTO salaryRequestDTO) {
        SalaryResponseDTO createdSalary = salaryService.addSalary(salaryRequestDTO);
        return new ResponseEntity<>(createdSalary, HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/updateSalary")
    public ResponseEntity<SalaryResponseDTO> updateSalary(@RequestParam Long id, @RequestBody SalaryUpdateRequestDTO salaryUpdateRequestDTO) {
        return new ResponseEntity<>(salaryService.updateSalary(id, salaryUpdateRequestDTO), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/deleteSalary")
    public ResponseEntity<Void> deleteExpense(@RequestParam Long id) {
        salaryService.deleteSalary(id);
        return ResponseEntity.noContent().build();
    }
}
