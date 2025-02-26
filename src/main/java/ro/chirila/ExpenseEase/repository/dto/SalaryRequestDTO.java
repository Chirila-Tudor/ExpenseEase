package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class SalaryRequestDTO {
    private Long userId;
    private Double totalSalary;
    private Double remainingSalary;
    private String date;
}
