package ro.chirila.ExpenseEase.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "salary_id", nullable = false)
    private Salary salary;
}
