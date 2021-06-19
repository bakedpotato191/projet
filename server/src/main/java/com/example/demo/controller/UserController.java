package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.mapstruct.dto.PostDto;
import com.example.demo.mapstruct.mappers.MapStructMapper;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.persistence.repository.PostRepository;
import com.example.demo.persistence.repository.UserRepository;
import com.example.demo.security.services.FileStorageService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private MapStructMapper mapstructMapper;
	
	@Autowired
	private FileStorageService storageService;

	@GetMapping("/{username}")
    public ResponseEntity<List<Object>> userPage(@PathVariable("username") String username){
		
		List<Object> list = new ArrayList<>();
		
		var user = userRepository.findByUsername(username);
		List<PostDto> posts = mapstructMapper.postListToPostListDto(postRepository.findAllByUtilisateurOrderByDateDesc(user));
		
		list.add(user);
		list.add(posts);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
    }

	@PostMapping(path="/upload")
	public ResponseEntity<MessageResponse> uploadFile(@RequestPart("photo") MultipartFile file, @RequestPart(name="description", required=false) String description) {
		var message = "";

		try {
			storageService.save(file, description);

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}

	@GetMapping("/photos/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
}
