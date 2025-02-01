package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class PiggyBankResponseDTO {
    private Long id;
    private Double amount;
    private String date;
    private String userId;
    private String salaryId;
}
