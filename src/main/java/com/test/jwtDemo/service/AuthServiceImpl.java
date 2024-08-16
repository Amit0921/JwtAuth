package com.test.jwtDemo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.jwtDemo.config.JwtService;
import com.test.jwtDemo.entity.User;
import com.test.jwtDemo.model.AuthRequestDTO;
import com.test.jwtDemo.model.RegisterResponseDTO;
import com.test.jwtDemo.model.UserResponseDTO;
import com.test.jwtDemo.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Logger logger;

	/* First-Step */
	// We need to validate our request (validate whether password & username is
	// correct)
	// Verify whether user present in the database
	// Which AuthenticationProvider -> DaoAuthenticationProvider (Inject)
	// We need to authenticate using authenticationManager injecting this
	// authenticationProvider
	/* Second-Step */
	// Verify whether userName and password is correct =>
	// UserNamePasswordAuthenticationToken
	// Verify whether user present in db
	// generateToken
	// Return the token
	@Override
	public UserResponseDTO login(AuthRequestDTO request) {
		Authentication auth;
		if (request.getEmail() == null || request.getPassword() == null)
			throw new NullPointerException("Incomplete information for login");
		try {
			auth = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		} catch (Exception e) {
			throw new UsernameNotFoundException("Wrong user credentials");
		}
		User user = (User) auth.getPrincipal();
		String jwt = jwtService.generateToken(user);

		return UserResponseDTO.builder().username(user.getUsername()).token(jwt).build();
	}

	@Override
	public RegisterResponseDTO register(User user) {
		if (user.getEmail() == null || user.getPassword() == null || user.getRole() == null
				|| user.getUsername() == null)
			throw new NullPointerException("Incomplete information for registration");

		Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser.isEmpty()) {
			logger.info("***Proceeding For Registration***");
			try {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				User savedUser = userRepository.save(user);
				Long userId = savedUser.getId();
				logger.info("***User Saved with userId : " + userId + " ***");
				return RegisterResponseDTO.builder().statusCode(HttpStatus.CREATED.value())
						.message("User successfully registered").build();
			} catch (Exception e) {
				throw new RuntimeException("Could not able to save user");
			}
		} else {
//            throw new RuntimeException("User already exists");
			return RegisterResponseDTO.builder().statusCode(HttpStatus.CONFLICT.value()).message("User already exists")
					.build();
		}
	}

}
