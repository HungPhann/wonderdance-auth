package tk.wonderdance.auth.payload.user.activation_code;

public class GetActivationCodeSuccessResponse {

    private boolean success;
    private String activate_code;

    public GetActivationCodeSuccessResponse(boolean success, String activate_code) {
        this.success = success;
        this.activate_code = activate_code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getActivate_code() {
        return activate_code;
    }

    public void setActivate_code(String activate_code) {
        this.activate_code = activate_code;
    }
}
