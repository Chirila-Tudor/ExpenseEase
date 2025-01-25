package ro.chirila.ExpenseEase.service;

import org.springframework.stereotype.Service;
import ro.chirila.ExpenseEase.repository.dto.PiggyBankRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.PiggyBankResponseDTO;
import ro.chirila.ExpenseEase.repository.dto.PiggyBankUpdateRequestDTO;

@Service
public interface PiggyBankService {

    PiggyBankResponseDTO addMoneyIntoPiggyBank(PiggyBankRequestDTO piggyBankRequestDTO);
    PiggyBankResponseDTO updatePiggyBankAmount(Long id, PiggyBankUpdateRequestDTO piggyBankUpdateRequestDTO);
    void deleteSaving(Long id);

}
