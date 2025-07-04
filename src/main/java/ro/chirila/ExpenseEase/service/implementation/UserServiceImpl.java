package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.exception.UserAlreadyDeactivatedException;
import ro.chirila.ExpenseEase.exception.UserAlreadyExistsException;
import ro.chirila.ExpenseEase.exception.UserNotFoundException;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.ChangePasswordDTO;
import ro.chirila.ExpenseEase.repository.dto.UserResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.UserSecurityDTO;
import ro.chirila.ExpenseEase.repository.entity.Role;
import ro.chirila.ExpenseEase.repository.entity.User;
import ro.chirila.ExpenseEase.service.SendEmailService;
import ro.chirila.ExpenseEase.service.UserService;
import ro.chirila.ExpenseEase.utils.PasswordGenerator;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static ro.chirila.ExpenseEase.utils.PasswordGenerator.*;

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
            if (user.getIsActive() && PasswordGenerator.verifyPassword(password, user.getPassword())) {
                return modelMapper.map(user, UserSecurityDTO.class);
            }
            if (!user.getIsActive() && PasswordGenerator.verifyPassword(password, user.getPassword())) {
                throw new UserAlreadyDeactivatedException("User was deactivated");
            }
        }
        throw new BadCredentialsException("Bad credentials.");
    }

    @Override
    public Boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        String username = changePasswordDTO.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));

        String hashedOldPassword = PasswordGenerator.hashPassword(changePasswordDTO.getOldPassword());
        if (!hashedOldPassword.equals(user.getPassword())) {
            return false;
        }

        String hashedNewPassword = PasswordGenerator.hashPassword(changePasswordDTO.getNewPassword());
        user.setPassword(hashedNewPassword);
        user.setIsFirstLogin(false);
        userRepository.save(user);

        return true;
    }

    @Override
    public String changePasswordSecurityCode(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            int length = 10;
            String securityCode = generateSecurityCode(length);
            String hashedSecurityCode = PasswordGenerator.hashSecurityCode(securityCode);
            user.setSecurityCode(hashedSecurityCode);
            userRepository.save(user);

            CompletableFuture.runAsync(() -> sendEmailService.sendSecurityCodeEmail(user.getEmail(),securityCode,username));

            return securityCode;
        }
        throw new BadCredentialsException("Wrong security code");
    }

    @Override
    public String getEmailByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found!"));
        return user.getEmail();
    }

    @Override
    public String requestNewPassword(String username, String securityCode) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        String hashedSecurityCode = PasswordGenerator.hashSecurityCode(securityCode);
        if (!user.getSecurityCode().equals(hashedSecurityCode)) {
            throw new BadCredentialsException("Invalid security code");
        }

        String plainTextPassword = generatePassword(12);
        user.setIsFirstLogin(true);
        user.setPassword(PasswordGenerator.hashPassword(plainTextPassword));
        userRepository.save(user);
        return plainTextPassword;
    }

    @Override
    public Boolean modifyUserActivity(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
        return user.getIsActive();
    }

    @Override
    public Boolean isFirstLogin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        return user.getIsFirstLogin();
    }


}
