package tk.wonderdance.auth.payload.user.activation_code;

public class GetActivationCodeResponse {

    private String activate_code;

    public GetActivationCodeResponse(String activate_code) {
        this.activate_code = activate_code;
    }

    public String getActivate_code() {
        return activate_code;
    }

    public void setActivate_code(String activate_code) {
        this.activate_code = activate_code;
    }
}
