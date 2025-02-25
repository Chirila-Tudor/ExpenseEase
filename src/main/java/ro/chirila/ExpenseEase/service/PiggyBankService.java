package ro.chirila.ExpenseEase.service;

import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.PiggyBankRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.PiggyBankResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.PiggyBankUpdateRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.TransactionResponseDTO;

import java.util.List;

@Service
public interface PiggyBankService {

    PiggyBankResponseDTO addMoneyIntoPiggyBank(PiggyBankRequestDTO piggyBankRequestDTO);
    PiggyBankResponseDTO updatePiggyBankAmount(Long id, PiggyBankUpdateRequestDTO piggyBankUpdateRequestDTO);
    void deleteSaving(Long id);
    List<PiggyBankResponseDTO> getAllSavings();
    PiggyBankResponseDTO getSavingById(Long savingId);
    double getTotalSavingsAmount();

}
