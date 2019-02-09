package tk.wonderdance.auth.payload.user.change_password;

public class ChangePasswordSuccessResponse {

    private boolean success;

    public ChangePasswordSuccessResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
