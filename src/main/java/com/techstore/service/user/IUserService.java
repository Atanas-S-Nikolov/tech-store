package com.techstore.service.user;

import com.techstore.exception.user.InvalidCredentialsException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.user.UserServiceException;
import com.techstore.model.User;

import java.util.Collection;

public interface IUserService {
    User createCustomer(User user) throws UserConstraintViolationException, UserServiceException;

    User createAdmin(User user) throws UserConstraintViolationException, UserServiceException;

    User getUser(String username, String password) throws UserConstraintViolationException, UserServiceException, InvalidCredentialsException;

    Collection<User> getAllUsers() throws UserConstraintViolationException, UserServiceException;

    User updateUser(User user) throws UserConstraintViolationException, UserServiceException, InvalidCredentialsException;

    void deleteUser(String username, String password) throws UserConstraintViolationException, UserServiceException, InvalidCredentialsException;
}
