package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class ExpenseRequestDTO {
    private String category;
    private Double amount;
    private String date;
    private Long userId;
    private Long salaryId;
}
