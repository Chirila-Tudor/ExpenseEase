package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class TransactionRequestDTO {
    private String description;
    private Double amount;
    private String date;
    private Long userId;
    private Long salaryId;
}
