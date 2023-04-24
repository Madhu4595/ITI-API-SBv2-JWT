package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.jwt.AUthenticationResponse;
import com.app.jwt.AuthenticationRequest;
import com.app.jwt.JwtUtil;
import com.app.repo.RoleRepository;
import com.app.service.UserService;


@RestController
public class HelloResource {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserService userService;

	@RequestMapping("/hello")
	public String hello() {
		return "Hello World";
	}

	@RequestMapping(value = "/home")
	public String home() {
		return "home";
	}

	@GetMapping(value = "/admin")
	public String adminHome() {
		return "admin";
	}

	@PostMapping(value = "addRole")
	public Role addRole(@RequestBody Role role) {
		return roleRepository.save(role);

	}

	@PostMapping(value = "addUser")
	public User addRole(@RequestBody User user) {
		return userService.addUser(user);
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthentivationToken(@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			e.printStackTrace();
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String jwt = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AUthenticationResponse(jwt));

	}
}
