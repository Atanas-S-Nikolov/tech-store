package com.techstore.service.user;

import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.model.User;
import com.techstore.model.enums.UserRole;

import java.util.Collection;

public interface IUserService {
    User createUserWithRole(User user, UserRole role);

    Collection<User> getAllUsers();

    User updateUser(User user) throws InvalidCredentialsException;

    void deleteUser(String username, String password) throws InvalidCredentialsException;
}
