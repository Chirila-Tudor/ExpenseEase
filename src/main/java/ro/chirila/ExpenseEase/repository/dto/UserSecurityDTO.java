package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;

@Data
public class UserSecurityDTO {
    private String username;
    private String role;
    private Boolean isFirstLogin;
}
