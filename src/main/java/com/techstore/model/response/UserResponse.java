package com.techstore.model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
public class UserResponse {
    @Getter
    private final String firstName;

    @Getter
    private final String lastName;

    @Getter
    private final String email;

    @Getter
    private final String phone;

    @Getter
    private final String username;

    public UserResponse() {
        this(null);
    }

    public UserResponse(String username) {
        this(null, null, null, null, username);
    }

    public UserResponse(String firstName, String lastName, String email, String phone, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.username = username;
    }
}
