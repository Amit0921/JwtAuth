package com.test.jwtDemo.model;

import com.test.jwtDemo.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
	private String name;
	private String email;
	private String password;
	private Role role;
}
