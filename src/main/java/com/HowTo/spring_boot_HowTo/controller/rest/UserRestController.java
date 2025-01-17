package com.HowTo.spring_boot_HowTo.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.UserDTO;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/users")
public class UserRestController {
	
	private UserServiceI userService;

	public UserRestController(UserServiceI userService) {
		super();
		this.userService = userService;
	}
	
	@Operation(summary = "Create a user")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "User created", 
					content = { @Content(mediaType = "application/json", 
				    schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameters", 
		    content = @Content)})
	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> postUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
		    description = "User to create", required = true,
		    content = @Content(mediaType = "application/json",
		      schema = @Schema(implementation = User.class),
		      examples = @ExampleObject(value = "{ \"password\": \"banana\" , \"enabled\": true, \"username\": \"HowToUser\", \"email\": \"banana@gmail.com\" , \"active\": true, \"birthDate\": [2001,1,21] }"))) 
		@Valid @RequestBody  User user, BindingResult result) {

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
	
	
	@Operation(summary = "Update a user")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Updated the user", 
					content = { @Content(mediaType = "application/json", 
				    schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameters", 
		    content = @Content), 
			@ApiResponse(responseCode = "404", description = "User not found", 
				    content = @Content) })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@Parameter(description = "id of user to be updated") @PathVariable("id") Long userId, 
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "User to update", required = true,
				    content = @Content(mediaType = "application/json",
				      schema = @Schema(implementation = User.class),
				      examples = @ExampleObject(value = "{ \"userName\": \"HowToUserChanged\", \"birthDate\": [2001,2,22], \"using2FA\": true }"))) 
			@Valid @RequestBody UserDTO user,
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
	
	
	@Operation(summary = "Get all users")
	@ApiResponse(responseCode = "200", description = "All Users", //no 204 because no user login needed for swagger
			content = { @Content(mediaType = "application/json")})
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
	
	
	//OpenAPI can do pageables
	@Operation(summary = "Get users by username / paging filter")
	@ApiResponse(responseCode = "200", description = "Paging Infos", 
			content = { @Content(mediaType = "application/json")})
		@GetMapping("/filter")
		public Page<User> filterUsers(@Parameter(description = "username searched for") @RequestParam("username") String username, @ParameterObject Pageable pageable) {
		     return userService.getAllUsers(username, pageable);
		}
	
	
	@Operation(summary = "Get a user by its id")
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "200", description = "Found the user", 
			content = { @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = User.class)) }),
	@ApiResponse(responseCode = "404", description = "User not found", 
		    content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<User>> getOneUser(@Parameter(description = "id of user to be searched") @PathVariable("id") Long userId) {
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
	
	
	@Operation(summary = "Delete a user by its id")
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "204", description = "Deleted the User", content = @Content),
	@ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@Parameter(description = "id of user to be deleted") @PathVariable("id") Long userId) {
		User user = userService.getUserById(userId);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		userService.delete(user);
		return ResponseEntity.noContent().build();
	}
}
