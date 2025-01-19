package ro.chirila.ExpenseEase.service;


import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.ChangePasswordDTO;
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
    Boolean changePassword(ChangePasswordDTO changePasswordDTO);
    String changePasswordSecurityCode(String username);
    String getEmailByUsername(String username);
    String requestNewPassword(String username, String securityCode);
    Boolean modifyUserActivity(Long userId);


}
