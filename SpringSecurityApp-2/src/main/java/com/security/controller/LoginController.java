package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.security.UserDetialsServiceImpl;
import com.security.dao.UserRepository;
import com.security.model.AuthenticationRequest;
import com.security.model.AuthenticationResponse;
import com.security.model.User;
import com.security.model.UserReponse;
import com.security.util.JwtUtil;

@RestController
@CrossOrigin(origins ="*",allowedHeaders = "*",exposedHeaders = "*")
public class LoginController {
	
	
	private AuthenticationManager authenticationManager;
	
	private UserDetialsServiceImpl userDetailsServiceImpl;
	
	private JwtUtil jwtUtil;
	
	private UserRepository userRepository;
	
	@Autowired
	public LoginController(AuthenticationManager authManager, UserDetialsServiceImpl userDetialsServiceImpl, JwtUtil jwtUtil,UserRepository userRepository) {
		this.authenticationManager = authManager;
		this.userDetailsServiceImpl = userDetialsServiceImpl;
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
	}
	
	
	@RequestMapping({"/"})
	public String home() {
		return "Home";
	}

	@RequestMapping("/hello")
	public String hello() {
		return "Hello world";
	}
	
	@RequestMapping("/some")
	public String somePeople() {
		return "Hello Some people.";
	}
	
	@RequestMapping("/admin")
	public String adminHello() {
		return "hello admin";
	}
	
	@GetMapping("/get-user-details")
	public UserReponse getUserDetails(String username) {
		User user = userRepository.getUserByUsername(username);
		UserReponse userResponse = new UserReponse();
		userResponse.setUsername(user.getUsername());
		return userResponse;
	}
	
	//@RequestMapping(value="/authenticate", method = RequestMethod.POST)
	@PostMapping(value="/authenticate")
	public ResponseEntity<?> createAuthenticationToken (@RequestBody  AuthenticationRequest authenticationRequest) throws Exception{
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}
		catch(BadCredentialsException e) {
			throw new Exception("Incorrect Username or password.",e);
		}
		
		final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
	}

}
