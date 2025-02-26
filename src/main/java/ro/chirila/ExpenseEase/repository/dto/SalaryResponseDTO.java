package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class SalaryResponseDTO {
    private Long id;
    private Double totalSalary;
    private Double remainingSalary;
    private String date;
    private Long userId;
}
