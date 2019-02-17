package tk.wonderdance.auth.payload.user.activate;

public class ActivateUserResponse {

    private boolean success;

    public ActivateUserResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
