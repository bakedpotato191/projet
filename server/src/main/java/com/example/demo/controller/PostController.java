package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.IllegalFormatException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.mapstruct.dto.CommentDto;
import com.example.demo.persistence.models.Post;
import com.example.demo.services.FileStorageService;
import com.example.demo.services.PhotoService;
import com.example.demo.services.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
public class PostController {
	
	@Autowired
	private FileStorageService storageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PhotoService photoService;
	
	@PostMapping(path="/create")
	public ResponseEntity<HttpStatus> uploadFile(@RequestPart("photo") MultipartFile file, @RequestPart(name="description", required=false) String description) throws IllegalFormatException, IOException {
		storageService.save(file, description);
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	@GetMapping("/view/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException, FileNotFoundException {
		Resource file = storageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@GetMapping(value= "/p/{postID}")
    public Post userPage(@PathVariable("postID") Long id) {
		return userService.getPostByID(id);
    }
	
	@PostMapping(path="/addcomment")
	public ResponseEntity<HttpStatus> addComment(@RequestBody @Valid final CommentDto comment) {
		photoService.addComment(comment);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping(path="/like")
	public ResponseEntity<HttpStatus> likePost(@RequestBody @Valid final String id) {
		photoService.likePhoto(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
