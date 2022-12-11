package com.techstore.service.user;

import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.exception.authentication.InvalidJWTException;
import com.techstore.exception.user.UserConstraintViolationException;
import com.techstore.exception.user.UserServiceException;
import com.techstore.model.User;
import com.techstore.model.response.JWTResponse;
import com.techstore.service.jwt.IJWTService;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface IUserService {
    public abstract User createCustomer(User user) throws UserConstraintViolationException, UserServiceException;

    public abstract User createAdmin(User user) throws UserConstraintViolationException, UserServiceException;

    public abstract Collection<User> getAllUsers() throws UserConstraintViolationException, UserServiceException;

    public abstract User updateUser(User user) throws UserConstraintViolationException, UserServiceException, InvalidCredentialsException;

    public abstract void deleteUser(String username, String password) throws UserConstraintViolationException, UserServiceException, InvalidCredentialsException;
}
