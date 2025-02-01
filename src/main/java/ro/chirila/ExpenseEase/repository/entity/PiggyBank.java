package ro.chirila.ExpenseEase.repository.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "savings")
@Data
public class PiggyBank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "salary_id", nullable = false)
    private Salary salary;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
