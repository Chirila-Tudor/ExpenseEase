package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class SalaryUpdateRequestDTO {
    private Long userId;
    private Double totalSalary;
}
