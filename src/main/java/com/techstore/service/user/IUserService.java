package com.techstore.service.user;

import com.techstore.exception.auth.InvalidCredentialsException;
import com.techstore.model.dto.EmailDto;
import com.techstore.model.dto.UpdateUserDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.response.GenericResponse;
import com.techstore.model.response.PageResponse;
import com.techstore.model.response.UserResponse;

public interface IUserService {
    UserResponse createUser(UserDto user);

    UserResponse getUser(String username);

    PageResponse<UserResponse> getAllUsers(Integer page, Integer size);

    UserResponse updateUser(UpdateUserDto user) throws InvalidCredentialsException;

    GenericResponse forgotPassword(EmailDto emailDto);

    void deleteUser(String username) throws InvalidCredentialsException;
}
