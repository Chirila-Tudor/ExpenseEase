package ro.chirila.ExpenseEase.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.ExpenseEase.exception.UserAlreadyExistsException;
import ro.chirila.ExpenseEase.repository.dto.ChangePasswordDTO;
import ro.chirila.ExpenseEase.repository.dto.UserRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.UserResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.UserSecurityDTO;
import ro.chirila.ExpenseEase.service.SendEmailService;
import ro.chirila.ExpenseEase.service.UserService;

import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SendEmailService sendEmailService;

    public UserController(UserService userService, SendEmailService sendEmailService) {
        this.userService = userService;
        this.sendEmailService = sendEmailService;
    }

    @Transactional
    @PostMapping("/addUser")
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserRequestDTO userRequest) {

        String username = userRequest.getUsername();
        String email = userRequest.getEmail();

        UserResponseDTO userResponseDTO;
        try {
            userResponseDTO = userService.registerUser(username, email);

        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public UserSecurityDTO login(@RequestParam(name = "username") String username, @RequestBody String hashPassword) {
        return userService.loginUser(username, hashPassword);
    }

    @Transactional
    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        boolean passwordChanged = userService.changePassword(changePasswordDTO);
        if (passwordChanged) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @PutMapping("/forgotPassword")
    public ResponseEntity<Boolean> changePasswordSecurityCode(@RequestParam("username") String username) {
        String securityCode = userService.changePasswordSecurityCode(username);
        String email = userService.getEmailByUsername(username);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/requestPassword")
    public ResponseEntity<Boolean> requestPassword(@RequestParam("username") String username,
                                                   @RequestBody String securityCode) {
        String newPassword = userService.requestNewPassword(username, securityCode);
        String email = userService.getEmailByUsername(username);
        CompletableFuture.runAsync(() -> sendEmailService.sendPasswordResetEmail(email, username, newPassword));
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/modifyUserActivity")
    public ResponseEntity<Boolean> modifyUserActivity(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(userService.modifyUserActivity(id), HttpStatus.OK);
    }

    @Transactional
    @GetMapping
    public UserResponseDTO getUserByUsername(@RequestParam(name = "username") String username) {
        return userService.getUserByUsername(username);
    }

    @Transactional
    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(@RequestBody String username) {
        return new ResponseEntity<>(userService.deleteUserByUsername(username), HttpStatus.OK);
    }
}
