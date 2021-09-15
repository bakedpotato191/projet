package com.example.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.FileStorageService;
import com.example.service.PublicationService;
import com.example.service.UserService;
import com.example.web.dto.response.AvatarResponse;
import com.example.web.dto.response.PublicationDto;
import com.example.web.dto.response.UserDto;
import com.example.web.exception.IncorrectFileExtensionException;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PublicationService postService;
	
	@Autowired
	private FileStorageService storageService;
	
	@Autowired
	private PublicationService publicationService;

	@GetMapping(value= "/info/{username}")
    public CompletableFuture<UserDto> getUser(@PathVariable("username") String username) throws InterruptedException, ExecutionException {
		return userService.getUserData(username);
    }

	@PostMapping(value= "/follow")
    public void follow(@RequestBody @Valid final String username) {
		userService.follow(username);	
    }
	
	@PostMapping(value= "/unfollow")
    public void unfollow(@RequestBody @Valid final String username) {
		userService.unfollow(username);
    }
	
	@GetMapping(value = "/favorites")
	public CompletableFuture<List<PublicationDto>> getFavorites(	@RequestParam Integer page, 
						            @RequestParam Integer size,
						            @RequestParam String sort) {
		return postService.getFavorites(page, size, sort);
	}
	
	@GetMapping(value = "/new")
	public  CompletableFuture<List<PublicationDto>> getNewPublications(@RequestParam Integer page, @RequestParam Integer size) {
		return publicationService.getNewPublications(page, size);
	}
	
	@GetMapping(value = "/posts/{username}")
	public CompletableFuture<List<PublicationDto>> getPosts( @PathVariable("username") String username,
												@RequestParam Integer page, 
									            @RequestParam Integer size,
									            @RequestParam String sort) {
		return postService.getUserPublications(username, page, size, sort);
	}
	
	@GetMapping(value = "/subscriptions/{username}")
	public CompletableFuture<List<UserDto>> getSubscriptions( @PathVariable("username") String username,
												@RequestParam Integer page, 
									            @RequestParam Integer size) throws InterruptedException {
		return userService.getSubscriptions(username, page, size);
	}
	
	@GetMapping(value = "/subscribers/{username}")
	public CompletableFuture<List<UserDto>> getSubscribers( @PathVariable("username") String username,
												@RequestParam Integer page, 
									            @RequestParam Integer size) {
		return userService.getSubscribers(username, page, size);
	}
	
	@GetMapping(value="/profile_picture")
	public CompletableFuture<AvatarResponse> getProfilePicture() {
		return userService.getProfilePicture();
	}
	
	@PostMapping(value= "/profile_picture")
    public CompletableFuture<AvatarResponse> setProfilePicture(@RequestPart("avatar") MultipartFile file) throws IncorrectFileExtensionException, IOException {
		return storageService.saveAvatar(file);

    }
	
	@DeleteMapping(value= "/reset_profile_picture")
    public CompletableFuture<AvatarResponse> resetProfilePicture() {
		return userService.resetProfilePicture();
    }
	
	@GetMapping("/profile_picture/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
		Resource file = storageService.loadAvatar(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
				.contentType(MediaType.IMAGE_JPEG)
				.body(file);
	}
	
	
}
