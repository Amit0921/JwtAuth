package com.test.jwtDemo.service;

import com.test.jwtDemo.entity.User;
import com.test.jwtDemo.model.AuthRequestDTO;
import com.test.jwtDemo.model.RegisterResponseDTO;
import com.test.jwtDemo.model.UserResponseDTO;

public interface AuthService {

	RegisterResponseDTO register(User user);

	UserResponseDTO login(AuthRequestDTO request);

}
