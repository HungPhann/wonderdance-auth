package tk.wonderdance.auth.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "auth_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Inheritance(strategy=InheritanceType.JOINED)
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(max = 256)
    private String password;

    private boolean activate = false;

    @NotNull
    @Size(max = 256)
    private String activate_code;

    public User(String email, String password, String activate_code) {
        this.email = email;
        this.password = password;
        this.activate_code = activate_code;
    }

    public User(String email, String password, String activate_code, boolean activate) {
        this.email = email;
        this.password = password;
        this.activate_code = activate_code;
        this.activate = activate;
    }

    public User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    public String getActivate_code() {
        return activate_code;
    }

    public void setActivate_code(String activate_code) {
        this.activate_code = activate_code;
    }
}
