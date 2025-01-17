package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class ExpenseResponseDTO {
    private Long id;
    private String category;
    private Double amount;
    private String date;
    private String user;
}
