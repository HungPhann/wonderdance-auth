package tk.wonderdance.auth.payload.user.reset_password;

public class ResetPasswordResponse {

    private String password;

    public ResetPasswordResponse(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
