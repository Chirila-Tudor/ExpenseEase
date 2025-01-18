package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.exception.UserAlreadyDeactivatedException;
import ro.chirila.ExpenseEase.exception.UserAlreadyExistsException;
import ro.chirila.ExpenseEase.exception.UserNotFoundException;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.UserResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.UserSecurityDTO;
import ro.chirila.ExpenseEase.repository.entity.Role;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.SendEmailService;
import ro.chirila.ExpenseEase.service.UserService;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static ro.chirila.ExpenseEase.utils.PasswordGenerator.generatePassword;
import static ro.chirila.ExpenseEase.utils.PasswordGenerator.hashPassword;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SendEmailService sendEmailService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, SendEmailService sendEmailService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.sendEmailService = sendEmailService;
    }

//    @Override
//    public UserResponseDTO addUser(String username, Role role, String email) {
//        if (userRepository.findByUsername(username).isPresent()) {
//            throw new UserAlreadyExistsException("User already exist!");
//        }
//        User user = new User();
//        user.setUsername(username);
//        user.setHasPassword(false);
//        user.setIsActive(false);
//        user.setRole(role);
//        user.setEmail(email);
//        userRepository.save(user);
//        return modelMapper.map(user, UserResponseDTO.class);
//    }

    @Override
    public UserResponseDTO registerUser(String username, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        String generatedPassword = generatePassword(12);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(Role.User);
        user.setPassword(hashPassword(generatedPassword));
        user.setHasPassword(true);
        user.setIsFirstLogin(true);
        user.setIsActive(true);
        userRepository.save(user);

        UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);

        CompletableFuture.runAsync(() -> {
            sendEmailService.sendPasswordEmail(generatedPassword, userResponseDTO);
        });
        userResponseDTO.setPassword(null);

        return userResponseDTO;
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return modelMapper.map(user, UserResponseDTO.class);

    }

    @Override
    public Boolean deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (user.getIsActive()) {
            return false;
        }
        userRepository.delete(user);
        return true;

    }

    @Override
    public UserSecurityDTO loginUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getIsActive() && password.equals(user.getPassword())) {
                return modelMapper.map(user, UserSecurityDTO.class);
            }
            if (!user.getIsActive() && password.equals(user.getPassword())) {
                throw new UserAlreadyDeactivatedException("User was deactivated");
            }

        }
        throw new BadCredentialsException("Bad credentials.");
    }
}
