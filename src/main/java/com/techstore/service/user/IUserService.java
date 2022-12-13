package com.techstore.service.user;

import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.user.UserServiceException;
import com.techstore.model.User;
import com.techstore.model.enums.UserRole;

import java.util.Collection;

public interface IUserService {
    User createUserWithRole(User user, UserRole role) throws UserConstraintViolationException, UserServiceException;

    Collection<User> getAllUsers() throws UserConstraintViolationException, UserServiceException;

    User updateUser(User user) throws UserConstraintViolationException, UserServiceException, InvalidCredentialsException;

    void deleteUser(String username, String password) throws UserConstraintViolationException, UserServiceException, InvalidCredentialsException;
}
