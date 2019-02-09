package tk.wonderdance.auth.payload.user.change_password;

public class ChangePasswordFailResponse {

    private boolean success;
    private int error_code;
    private String message;

    public ChangePasswordFailResponse(boolean success, int error_code, String message) {
        this.success = success;
        this.error_code = error_code;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
