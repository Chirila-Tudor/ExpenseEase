package ro.chirila.ExpenseEase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ro.chirila.ExpenseEase.repository.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
