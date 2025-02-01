package ro.chirila.ExpenseEase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.chirila.ExpenseEase.repository.entity.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
}
