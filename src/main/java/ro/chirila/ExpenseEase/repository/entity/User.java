package ro.chirila.ExpenseEase.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Data
@Setter
@Getter
public class User {

    //region Constructor
    public User(Boolean hasPassword, Long id, String password, String username, Role role, Boolean isFirstLogin,
                Boolean isActive, String securityCode, String email) {
        this.hasPassword = hasPassword;
        this.id = id;
        this.password = password;
        this.username = username;
        this.role = role;
        this.isFirstLogin = isFirstLogin;
        this.isActive = isActive;
        this.securityCode = securityCode;
        this.email = email;
    }

    public User(){}
    //endregion

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_person")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private Boolean hasPassword;

    private Role role;

    private Boolean isActive;

    private Boolean isFirstLogin;

    private String securityCode;

    private String email;
}
