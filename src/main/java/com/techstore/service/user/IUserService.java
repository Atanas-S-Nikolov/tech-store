package com.techstore.service.user;

import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.model.dto.UserDto;
import com.techstore.model.enums.UserRole;
import com.techstore.model.response.UserResponse;

import java.util.Collection;

public interface IUserService {
    UserResponse createUserWithRole(UserDto user, UserRole role);

    Collection<UserResponse> getAllUsers();

    UserResponse updateUser(UserDto user) throws InvalidCredentialsException;

    void deleteUser(String username, String password) throws InvalidCredentialsException;
}
