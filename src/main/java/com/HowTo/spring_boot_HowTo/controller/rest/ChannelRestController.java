package com.HowTo.spring_boot_HowTo.controller.rest;
import java.sql.Timestamp;
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

import com.HowTo.spring_boot_HowTo.model.Channel;
import com.HowTo.spring_boot_HowTo.service.ChannelServiceI;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value = "/api/channels")
public class ChannelRestController {

	private ChannelServiceI channelService;
	
	public ChannelRestController(ChannelServiceI channelService) {
		this.channelService = channelService;
	}
	
	
	@PostMapping(value = "/", consumes = "application/json")
	public ResponseEntity<?> postChannel(@Valid @RequestBody Channel channel, BindingResult result, 
			@RequestParam("userId") Long userId) {

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
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateChannel(@PathVariable("id") Long channelId, @Valid @RequestBody Channel channel,
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
	
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Channel>> getOneChannel(@PathVariable("id") Long channelId) {
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
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteChannel(@PathVariable("id") Long channelId) {
		Channel channel = channelService.getChannelById(channelId);
		if (channel == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		channelService.delete(channel);
		return ResponseEntity.noContent().build();
	}
}
