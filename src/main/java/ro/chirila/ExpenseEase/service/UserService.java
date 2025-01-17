package ro.chirila.ExpenseEase.service;


import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.UserResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.Role;

@Service
public interface UserService {

    UserResponseDTO addUser(String username, Role role, String email);
}
