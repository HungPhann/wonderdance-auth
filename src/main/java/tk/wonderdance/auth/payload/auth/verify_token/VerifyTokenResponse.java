package tk.wonderdance.auth.payload.auth.verify_token;

import io.jsonwebtoken.Claims;

public class VerifyTokenResponse {

    private Claims payload;

    public VerifyTokenResponse(Claims payload) {
        this.payload = payload;
    }

    public Claims getPayload() {
        return payload;
    }

    public void setPayload(Claims payload) {
        this.payload = payload;
    }
}
