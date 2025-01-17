package ro.chirila.ExpenseEase.repository.dto;

import lombok.Data;
import ro.chirila.ExpenseEase.repository.entity.Role;

import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Boolean isActive;
    private List<ExpenseResponseDTO> expenses;

}
