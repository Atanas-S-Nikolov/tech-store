package com.techstore.model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class JWTResponse {
    @Getter
    private final String accessToken;

    @Getter
    private final String refreshToken;

    public JWTResponse() {
        this(null, null);
    }

    public JWTResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
