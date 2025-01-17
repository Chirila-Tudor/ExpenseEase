package ro.chirila.ExpenseEase.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.ExpenseEase.exception.UserAlreadyExistsException;
import ro.chirila.ExpenseEase.repository.dto.UserRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.UserResponseDTO;
import ro.chirila.ExpenseEase.repository.entity.Role;
import ro.chirila.ExpenseEase.service.UserService;

import java.util.concurrent.CompletableFuture;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/addUser")
    public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserRequestDTO userRequest) {
        String username = userRequest.getUsername();
        Role role = userRequest.getRole();
        String email = userRequest.getEmail();

        UserResponseDTO userResponseDTO;
        try {
            userResponseDTO = userService.addUser(username, role, email);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

//        UserExistsDTO userExistsDTO = userService.setPasswordForUser(username, role);
//        if (userExistsDTO != null) {
//            CompletableFuture.runAsync(() -> sendEmailService.sendPasswordForHospitalPersonal(userExistsDTO, userRequest));
//        }

        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }
}
