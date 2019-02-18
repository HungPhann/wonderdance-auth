package tk.wonderdance.auth.payload.user.activate;

import javax.validation.constraints.NotNull;

public class ActivateUserRequest {

    @NotNull
    private String activate_code;

    public ActivateUserRequest(@NotNull String activate_code) {
        this.activate_code = activate_code;
    }

    public ActivateUserRequest(){}

    public String getActivate_code() {
        return activate_code;
    }

    public void setActivate_code(String activate_code) {
        this.activate_code = activate_code;
    }
}
