package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;
import ro.chirila.ExpenseEase.repository.entity.Role;

@Data
public class UserRequestDTO {
    private String username;
    private String email;
}
