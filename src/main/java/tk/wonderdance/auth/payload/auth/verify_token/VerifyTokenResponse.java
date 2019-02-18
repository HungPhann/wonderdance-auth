package tk.wonderdance.auth.payload.auth.verify_token;

import io.jsonwebtoken.Claims;

public class VerifyTokenResponse {

    private boolean success;
    private Claims payload;

    public VerifyTokenResponse(boolean success, Claims payload) {
        this.success = success;
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Claims getPayload() {
        return payload;
    }

    public void setPayload(Claims payload) {
        this.payload = payload;
    }
}