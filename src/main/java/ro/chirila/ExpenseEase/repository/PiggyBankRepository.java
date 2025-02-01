package ro.chirila.ExpenseEase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.chirila.ExpenseEase.repository.entity.PiggyBank;

public interface PiggyBankRepository extends JpaRepository<PiggyBank, Long> {
}
