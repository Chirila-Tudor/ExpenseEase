package ro.chirila.ExpenseEase.service;

import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.UserResponseDTO;


@Service
public interface SendEmailService {

    void sendPasswordEmail(String password, UserResponseDTO userResponseDTO);
}
