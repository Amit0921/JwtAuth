package com.test.jwtDemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@PreAuthorize("hasRole('MEMBER')")
public class MemberController {

	@GetMapping
	@PreAuthorize("hasAuthority('admin:read')")
	public String getAdmin() {
		return "Secured Endpoint :: GET - Member controller";
	}

	@PostMapping
	@PreAuthorize("hasAuthority('admin:create')")
	public String post() {
		return "Secured Endpoint :: POST - Member controller";
	}
}
