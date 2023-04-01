package com.techstore.service.user;

import com.techstore.exception.authentication.InvalidCredentialsException;
import com.techstore.model.dto.UpdateUserDto;
import com.techstore.model.dto.UserDto;
import com.techstore.model.response.PageResponse;
import com.techstore.model.response.UserResponse;

public interface IUserService {
    UserResponse createUser(UserDto user);

    PageResponse<UserResponse> getAllUsers(Integer page, Integer size);

    UserResponse updateUser(UpdateUserDto user) throws InvalidCredentialsException;

    void deleteUser(String username) throws InvalidCredentialsException;
}
