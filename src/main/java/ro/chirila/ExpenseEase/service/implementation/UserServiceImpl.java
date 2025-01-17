package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.exception.UserAlreadyExistsException;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.UserResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.Role;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDTO addUser(String username, Role role, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User already exist!");
        }
        User user = new User();
        user.setUsername(username);
        user.setHasPassword(false);
        user.setIsActive(false);
        user.setRole(role);
        user.setEmail(email);
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
