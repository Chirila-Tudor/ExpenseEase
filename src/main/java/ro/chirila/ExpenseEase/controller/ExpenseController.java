package ro.chirila.ExpenseEase.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.chirila.ExpenseEase.repository.dto.ExpenseRequestDTO;
import ro.chirila.ExpenseEase.repository.dto.ExpenseResponseDTO;
import ro.chirila.ExpenseEase.service.ExpenseService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Transactional
    @PostMapping("/addExpense")
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody ExpenseRequestDTO expenseRequestDTO) {
        ExpenseResponseDTO expenseResponseDTO = expenseService.addExpense(expenseRequestDTO);
        return ResponseEntity.ok(expenseResponseDTO);
    }

    @GetMapping("/getAllExpenses")
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses() {
        return new ResponseEntity<>(expenseService.getAllExpenses(), HttpStatus.OK);
    }
    @PutMapping("/updateExpenses")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@RequestParam Long id, @RequestBody ExpenseRequestDTO expenseRequestDTO) {
        return new ResponseEntity<>(expenseService.updateExpense(id, expenseRequestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/deleteExpense")
    public ResponseEntity<Void> deleteExpense(@RequestParam Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
