package ro.chirila.ExpenseEase.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.ExpenseEase.repository.dto.*;
import ro.chirila.ExpenseEase.service.PiggyBankService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/savings")
public class PiggyBankController {

    private final PiggyBankService piggyBankService;

    public PiggyBankController(PiggyBankService piggyBankService) {
        this.piggyBankService = piggyBankService;
    }

    @Transactional
    @PostMapping("/addPiggyBankAmount")
    public ResponseEntity<PiggyBankResponseDTO> addPiggyBankAmmount(@RequestBody PiggyBankRequestDTO piggyBankRequestDTO) {
        PiggyBankResponseDTO piggyBankResponseDTO = piggyBankService.addMoneyIntoPiggyBank(piggyBankRequestDTO);
        return ResponseEntity.ok(piggyBankResponseDTO);
    }

    @Transactional
    @PutMapping("/updatePiggyBank")
    public ResponseEntity<PiggyBankResponseDTO> updatePiggyBank(@RequestParam Long id, @RequestBody PiggyBankUpdateRequestDTO piggyBankUpdateRequestDTO) {
        return new ResponseEntity<>(piggyBankService.updatePiggyBankAmount(id, piggyBankUpdateRequestDTO), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/deletePiggyBank")
    public ResponseEntity<Void> deletePiggyBank(@RequestParam Long id) {
        piggyBankService.deleteSaving(id);
        return ResponseEntity.noContent().build();
    }
}
