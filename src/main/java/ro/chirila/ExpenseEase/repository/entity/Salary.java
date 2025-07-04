package ro.chirila.ExpenseEase.repository.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "salaries")
@Data
public class Salary {

    public Salary() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double totalSalary;

    @Column(nullable = false)
    private Double remainingSalary;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "salary", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "salary", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    @OneToMany(mappedBy = "salary", cascade = CascadeType.ALL)
    private List<PiggyBank> piggyBankAmount;

}
