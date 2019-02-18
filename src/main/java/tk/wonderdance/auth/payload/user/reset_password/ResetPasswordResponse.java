package tk.wonderdance.auth.payload.user.reset_password;

public class ResetPasswordResponse {

    private boolean success;
    private String password;

    public ResetPasswordResponse(boolean success, String password) {
        this.success = success;
        this.password = password;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}