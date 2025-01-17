package com.HowTo.spring_boot_HowTo.controller.rest;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/channels")
public class ChannelRestController {

	private ChannelServiceI channelService;
	
	public ChannelRestController(ChannelServiceI channelService) {
		this.channelService = channelService;
	}
	
	@Operation(summary = "Create a channel")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "Channel created", 
					content = { @Content(mediaType = "application/json", 
				    schema = @Schema(implementation = Channel.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameters", 
		    content = @Content)})
	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> postChannel(@io.swagger.v3.oas.annotations.parameters.RequestBody(
		    description = "Channel to create", required = true,
		    content = @Content(mediaType = "application/json",
		      schema = @Schema(implementation = Channel.class),
		      examples = @ExampleObject(value = "{ \"channelname\": \"HowToSwagger\",  \"description\": \"This Channel shows how to do OpenAPI Swagger related things\" }"))) 
			@Valid @RequestBody Channel channel, BindingResult result, 
			@Parameter(description = "id of creator/channel") @RequestParam("userId") Long userId) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		channel.setCreationDate(LocalDate.now());
		channel.setChannelId(userId);
		Channel savedChannel = channelService.saveChannel(channel, userId);
		EntityModel<Channel> entityModel = EntityModel.of(savedChannel);
		Link channelLink = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(ChannelRestController.class).getOneChannel(savedChannel.getChannelId()))
				.withSelfRel();
		entityModel.add(channelLink);

		return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update a channel")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Updated the channel", 
					content = { @Content(mediaType = "application/json", 
				    schema = @Schema(implementation = Channel.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameters", 
		    content = @Content), 
			@ApiResponse(responseCode = "404", description = "Channel not found", 
				    content = @Content) })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateChannel(@Parameter(description = "id of channel") @PathVariable("id") Long channelId, 
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "Channel to update", required = true,
				    content = @Content(mediaType = "application/json",
				      schema = @Schema(implementation = Channel.class),
				      examples = @ExampleObject(value = "{ \"channelname\": \"HowToSport\",  \"description\": \"This Channel shows how to do SPORT related things\" }"))) 
			@Valid @RequestBody Channel channel,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}

		Channel oldChannel = channelService.getChannelById(channelId);

		if (oldChannel == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (channel.getChannelname() != null) {
			oldChannel.setChannelname(channel.getChannelname());
		}
		if (channel.getDescription() != null) {
			oldChannel.setDescription(channel.getDescription());
		}
		if (channel.getCreationDate() != null) {
			oldChannel.setCreationDate(channel.getCreationDate());
		}

		Channel updatedChannel = channelService.updateChannel(oldChannel);
		EntityModel<Channel> entityModel = EntityModel.of(updatedChannel);
		Link channelLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ChannelRestController.class)
				.getOneChannel(updatedChannel.getChannelId())).withSelfRel();
		entityModel.add(channelLink);
		return new ResponseEntity<>(entityModel, HttpStatus.OK);

	}

	
	@Operation(summary = "Get all channels")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "All channels", 
				content = { @Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "204", description = "No channels", content = @Content)})
	@GetMapping("/")
	public ResponseEntity<?> getAllChannels() {
		List<Channel> allChannels = channelService.getAllChannels();
		if (allChannels.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		List<EntityModel<Channel>> channelModels = new ArrayList();
		for (Channel channel : allChannels) {
			EntityModel<Channel> entityModel = EntityModel.of(channel);
			Link channelLink = WebMvcLinkBuilder.linkTo(
					WebMvcLinkBuilder.methodOn(ChannelRestController.class).getOneChannel(channel.getChannelId()))
					.withSelfRel();
			entityModel.add(channelLink);
			channelModels.add(entityModel);
		}
		Link listLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ChannelRestController.class).getAllChannels()).withSelfRel();

		return new ResponseEntity<>(CollectionModel.of(channelModels, listLink), HttpStatus.OK);
	}
	
	
	@Operation(summary = "Get channels by channelname / paging filter")
	@ApiResponse(responseCode = "200", description = "Paging Infos", 
			content = { @Content(mediaType = "application/json")})
	//OpenAPI can do it
	@GetMapping("/filter")
	public Page<Channel> filterChannels(@Parameter(description = "channelname searched for") @RequestParam("channelname") String channelname, @ParameterObject Pageable pageable) {
	     return channelService.getAllChannels(channelname, pageable);
	}
	
	
	@Operation(summary = "Get a channel by its id")
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "200", description = "Found the channel", 
			content = { @Content(mediaType = "application/json", 
		    schema = @Schema(implementation = Channel.class)) }),
	@ApiResponse(responseCode = "404", description = "Channel not found", 
		    content = @Content) })
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Channel>> getOneChannel(@Parameter(description = "id of channel to be searched") @PathVariable("id") Long channelId) {
		Channel channel = channelService.getChannelById(channelId);
		if (channel == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Channel> entityModel = EntityModel.of(channel);
		Link channelLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ChannelRestController.class).getOneChannel(channelId))
				.withSelfRel();
		entityModel.add(channelLink);

		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}
	
	
	@Operation(summary = "Delete a channel by its id")
	@ApiResponses(value = { 
	@ApiResponse(responseCode = "204", description = "Deleted the channel", content = @Content),
	@ApiResponse(responseCode = "404", description = "Channel not found", content = @Content)})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteChannel(@Parameter(description = "id of channel to be deleted") @PathVariable("id") Long channelId) {
		Channel channel = channelService.getChannelById(channelId);
		if (channel == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		channelService.delete(channel);
		return ResponseEntity.noContent().build();
	}
}
