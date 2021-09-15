package com.example.web.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.CommentService;
import com.example.web.dto.request.CommentReqDto;
import com.example.web.dto.response.CommentResDto;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@PostMapping("/add")
	public ResponseEntity<HttpStatus> addComment(@RequestBody @Valid final CommentReqDto comment) {
		commentService.addComment(comment);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") Long id) {
		commentService.deleteComment(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/all/{postID}")
	public CompletableFuture<List<CommentResDto>> getPosts( @PathVariable("postID") Long id,
												@RequestParam Integer page, 
									            @RequestParam Integer size,
									            @RequestParam String sort) {
		return commentService.listComments(id, page, size, sort);
	}
}
