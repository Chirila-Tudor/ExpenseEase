package ro.chirila.ExpenseEase.repository.entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "expenses")
@Data
public class Expense {


    //region Constructor
    public Expense(Long id, String category, Double amount, String date, User user) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.user = user;
    }

    public Expense() {
    }
    //endregion

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
