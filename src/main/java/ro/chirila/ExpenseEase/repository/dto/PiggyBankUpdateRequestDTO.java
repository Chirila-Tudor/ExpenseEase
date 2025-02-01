package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class PiggyBankUpdateRequestDTO {
    private Double amount;
    private Long userId;
    private Long salaryId;
}
