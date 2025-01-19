package ro.chirila.ExpenseEase.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.TransactionRepository;
import ro.chirila.ExpenseEase.repository.UserRepository;
import ro.chirila.ExpenseEase.repository.dto.TransactionRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionResponseDTO;
import ro.chirila.ExpenseEase.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    public final TransactionRepository transactionRepository;
    public final UserRepository userRepository;
    public final ModelMapper modelMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TransactionResponseDTO addTransaction(TransactionRequestDTO transactionRequestDTO) {
//        User user = userRepository.findById(transactionRequestDTO.getUserId())
//                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + transactionRequestDTO.getUserId()));
//
//        Transaction transaction = modelMapper.map(transactionRequestDTO, Transaction.class);
//        transaction.setUser(user);
//
//        Transaction savedTransaction = transactionRepository.save(transaction);
//        return modelMapper.map(savedTransaction, TransactionResponseDTO.class);
        return null;
    }
}
