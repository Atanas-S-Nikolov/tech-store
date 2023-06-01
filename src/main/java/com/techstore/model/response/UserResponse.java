package com.techstore.model.response;

import com.techstore.model.enums.UserRole;
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
    private final String address;

    @Getter
    private final String username;

    @Getter
    private final UserRole role;

    public UserResponse() {
        this(null);
    }

    public UserResponse(String username) {
        this(username, null, null, null, null, null, null);
    }

    public UserResponse(String username, String firstName, String lastName, String email, String phone, String address, UserRole role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }
}
