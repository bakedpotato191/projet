package com.example.demo.controller;

import java.io.IOException;
import java.util.IllegalFormatException;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.mapstruct.dto.CommentDto;
import com.example.demo.persistence.models.Post;
import com.example.demo.services.FileStorageService;
import com.example.demo.services.PostService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
public class PostController {

	@Autowired
	private FileStorageService storageService;
	
	@Autowired
	private PostService postService;

	@PostMapping("/create")
	public ResponseEntity<HttpStatus> createPost(@RequestPart("photo") MultipartFile file, @RequestPart(name="description", required=false) String description) throws IllegalFormatException, IOException {
		storageService.save(file, description);
		return new ResponseEntity<>(HttpStatus.CREATED);

	}
	
	@GetMapping("/view/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
		Resource file = storageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
				.contentType(MediaType.IMAGE_JPEG)
				.body(file);
	}
	
	@GetMapping("/{id}")
    public Post userPage(@PathVariable("id") Long id) {
		return postService.getPostByID(id);
    }
	
	@PostMapping("/addcomment")
	public ResponseEntity<HttpStatus> addComment(@RequestBody @Valid final CommentDto comment) {
		postService.addComment(comment);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/like")
	public ResponseEntity<HttpStatus> likePost(@RequestBody @Valid final Long id) {
		postService.likeThePost(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/dislike")
	public ResponseEntity<HttpStatus> dislikePost(@RequestBody @Valid final Long id) {
		postService.dislikeThePost(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deletePost(@PathVariable("id") Long id) {
			postService.deletePost(id);
			return new ResponseEntity<>(HttpStatus.OK);
	}
}
