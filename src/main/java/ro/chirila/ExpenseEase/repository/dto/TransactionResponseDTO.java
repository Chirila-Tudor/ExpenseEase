package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class TransactionResponseDTO {
    private Long id;
    private String description;
    private Double amount;
    private String date;
    private String userId;
    private String salaryId;
}
