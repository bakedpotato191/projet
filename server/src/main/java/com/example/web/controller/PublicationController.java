package com.example.web.controller;

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

import com.example.service.FileStorageService;
import com.example.service.PublicationService;
import com.example.web.dto.response.PublicationDto;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/publication")
public class PublicationController {

	@Autowired
	private FileStorageService storageService;
	
	@Autowired
	private PublicationService publicationService;

	@GetMapping("/{id}")
    public PublicationDto getById(@PathVariable("id") Long id) {
		return publicationService.getPublicationByID(id);
    }
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deletePost(@PathVariable("id") Long id) {
		publicationService.deletePublication(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/view/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
		Resource file = storageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
				.contentType(MediaType.IMAGE_JPEG)
				.body(file);
	}
	
	@PostMapping("/like")
	public ResponseEntity<HttpStatus> likePost(@RequestBody @Valid final Long id) {
		publicationService.like(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/dislike")
	public ResponseEntity<HttpStatus> dislikePost(@RequestBody @Valid final Long id) {
		publicationService.dislike(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/create")
	public ResponseEntity<HttpStatus> createPost(@RequestPart("photo") MultipartFile file, @RequestPart(name="description", required=false) String description) throws IllegalFormatException, IOException {
		storageService.save(file, description);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	

}
