package com.HowTo.spring_boot_HowTo.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.UserDTO;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/users")
public class UserRestController {
	
	private UserServiceI userService;

	public UserRestController(UserServiceI userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> postUser(@Valid @RequestBody User user, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		User savedUser = userService.saveUser(user);
		EntityModel<User> entityModel = EntityModel.of(savedUser);
		Link userLink = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(UserRestController.class).getOneUser(savedUser.getUserId()))
				.withSelfRel();
		entityModel.add(userLink);

		return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") Long userId, @Valid @RequestBody UserDTO user,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}

		User oldUser = userService.getUserById(userId);

		if (oldUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (user.getUserName() != null) {
			oldUser.setUsername(user.getUserName());
		}
		if (user.getBirthDate() != null) {
			oldUser.setBirthDate(user.getBirthDate());
		}
		if(user.isUsing2FA() != oldUser.isUsing2FA()) {
			oldUser.setUsing2FA(user.isUsing2FA());
		}

		User updatedUser = userService.updateUser(oldUser);
		EntityModel<User> entityModel = EntityModel.of(updatedUser);
		Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class)
				.getOneUser(updatedUser.getUserId())).withSelfRel();
		entityModel.add(userLink);
		return new ResponseEntity<>(entityModel, HttpStatus.OK);

	}
	@GetMapping("/")
	public ResponseEntity<?> getAllUsers() {
		List<User> allUsers = userService.getAllUsers();
		if (allUsers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		List<EntityModel<User>> userModels = new ArrayList();
		for (User user : allUsers) {
			EntityModel<User> entityModel = EntityModel.of(user);
			Link userLink = WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(UserRestController.class).getOneUser(user.getUserId()))
					.withSelfRel();
			entityModel.add(userLink);
			userModels.add(entityModel);
		}
		Link listLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class).getAllUsers()).withSelfRel();

		return new ResponseEntity<>(CollectionModel.of(userModels, listLink), HttpStatus.OK);
	}
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<User>> getOneUser(@PathVariable("id") Long userId) {
		User user = userService.getUserById(userId);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<User> entityModel = EntityModel.of(user);
		Link userLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class).getOneUser(userId))
				.withSelfRel();
		entityModel.add(userLink);

		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
		User user = userService.getUserById(userId);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		userService.delete(user);
		return ResponseEntity.noContent().build();
	}
}
