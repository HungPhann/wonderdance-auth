package tk.wonderdance.auth.payload.user.change_password;

public class ChangePasswordResponse {

    private boolean success;

    public ChangePasswordResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
