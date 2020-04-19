package com.nayak.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nayak.model.request.UpdateUserDetailRequest;
import com.nayak.model.request.UserDetailsRequest;
import com.nayak.model.response.UserRest;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {
	
	Map<String,UserRest> userMap;
	
	@GetMapping
	public String getUser(@RequestParam(value="page", defaultValue ="1") int page,
			@RequestParam(value="limit", defaultValue="50") int limit,
			@RequestParam(value="sort", required= false) String sort) {
		return "Get user called with page ="+page+" limit ="+limit;
	}
	
	@GetMapping(path="/{userId}", 
			produces = {
					MediaType.APPLICATION_XML_VALUE, 
					MediaType.APPLICATION_JSON_VALUE
					})
	public ResponseEntity<UserRest> getUser(@PathVariable String userId) {
		if(userMap.containsKey(userId)) {
			return new ResponseEntity<>(userMap.get(userId),HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
	}
	
	@PostMapping(consumes = {
					MediaType.APPLICATION_XML_VALUE, 
					MediaType.APPLICATION_JSON_VALUE
					},
			produces = {
					MediaType.APPLICATION_XML_VALUE, 
					MediaType.APPLICATION_JSON_VALUE
					})
	public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailsRequest userReq) {
		
		UserRest user = new UserRest();
		user.setFirstName(userReq.getFirstName()); 
		user.setLastName(userReq.getLastName());
		user.setEmail(userReq.getEmail());
		
		String userId = UUID.randomUUID().toString();
		user.setUserId(userId);
		
		if(userMap == null) userMap = new HashMap();
		userMap.put(userId, user);
		
		return new ResponseEntity<>(user,HttpStatus.OK);
		
	}
	
	@PutMapping(path="/{userId}", consumes = {
			MediaType.APPLICATION_XML_VALUE, 
			MediaType.APPLICATION_JSON_VALUE
			},
	produces = {
			MediaType.APPLICATION_XML_VALUE, 
			MediaType.APPLICATION_JSON_VALUE
			})
	public UserRest updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserDetailRequest userReq) {
		UserRest storedUserDetails = userMap.get(userId);
		storedUserDetails.setFirstName(userReq.getFirstName());
		storedUserDetails.setLastName(userReq.getLastName());
		userMap.put(userId, storedUserDetails);
		return storedUserDetails;
	}
	
	@DeleteMapping(path ="{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable String id) {
		userMap.remove(id);
		return ResponseEntity.noContent().build();
	}
	

}
