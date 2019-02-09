package tk.wonderdance.auth.payload.user.create;

public class CreateUserSuccessResponse {

    private boolean success;
    private long user_id;
    private String activate_code;

    public CreateUserSuccessResponse(boolean success, long user_id, String activate_code) {
        this.success = success;
        this.user_id = user_id;
        this.activate_code = activate_code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getActivate_code() {
        return activate_code;
    }

    public void setActivate_code(String activate_code) {
        this.activate_code = activate_code;
    }
}
