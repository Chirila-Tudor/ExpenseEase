package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class PiggyBankRequestDTO {
    private Double amount;
    private String date;
    private Long userId;
    private Long salaryId;
}
