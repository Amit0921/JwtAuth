package com.test.jwtDemo.config;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.jwtDemo.entity.User;
import com.test.jwtDemo.model.AuthRequestDTO;
import com.test.jwtDemo.model.RegisterResponseDTO;
import com.test.jwtDemo.model.UserResponseDTO;
import com.test.jwtDemo.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private Logger logger;
	
	@PostMapping("/login")
	public ResponseEntity<UserResponseDTO> login(@RequestBody AuthRequestDTO request){
		logger.info(request.toString());
		return new ResponseEntity<UserResponseDTO>(authService.login(request),HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponseDTO> register(@RequestBody User user){
		RegisterResponseDTO registerResponse = authService.register(user);
		return new ResponseEntity<RegisterResponseDTO>(registerResponse, HttpStatus.valueOf(registerResponse.getStatusCode()));
	}
	
	@GetMapping("/test")
	public ResponseEntity<?> test(){
		logger.info("*** This is Open Api ***");
		return new ResponseEntity<String>("Api tested successfully",HttpStatus.OK);
	}

}
