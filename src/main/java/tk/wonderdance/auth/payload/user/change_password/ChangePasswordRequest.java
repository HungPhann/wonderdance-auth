package tk.wonderdance.auth.payload.user.change_password;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ChangePasswordRequest {

    @NotNull
    @NotBlank
    private String old_password;

    @NotNull
    @NotBlank
    private String new_password;

    public ChangePasswordRequest(@NotNull String old_password, @NotNull String new_password) {
        this.old_password = old_password;
        this.new_password = new_password;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
