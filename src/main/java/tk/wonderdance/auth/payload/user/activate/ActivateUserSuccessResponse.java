package tk.wonderdance.auth.payload.user.activate;

public class ActivateUserSuccessResponse {

    private boolean success;

    public ActivateUserSuccessResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
