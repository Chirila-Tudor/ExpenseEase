package ro.chirila.ExpenseEase.service;


import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.UserRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.UserResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.UserSecurityDTO;
import ro.chirila.ExpenseEase.repository.entity.Role;

@Service
public interface UserService {

    UserResponseDTO registerUser(String username, String email);
    UserResponseDTO getUserByUsername(String username);
    Boolean deleteUserByUsername(String username);
    UserSecurityDTO loginUser(String username, String password);


}
