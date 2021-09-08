package com.example.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.rest.model.Publication;
import com.example.service.FileStorageService;
import com.example.service.PublicationService;
import com.example.service.UserService;
import com.example.web.dto.response.AvatarResponse;
import com.example.web.exception.IncorrectFileExtensionException;

@Service
@Transactional
public class FileStorageServiceImpl implements FileStorageService {

	private final Path avatars = Paths.get("avatars");
	private final Path uploads = Paths.get("uploads");
	private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/webp", "image/bmp", "image/jpg");

	@Autowired
	private PublicationService postService;
	
	@Autowired
	private UserService userService;

	@Override
	public void init() throws IOException {

		if (!Files.isDirectory(uploads)) {
			Files.createDirectory(uploads);
		}
		
		if (!Files.isDirectory(avatars)) {
			Files.createDirectory(avatars);
		}
	}

	@Override
	public Publication save(MultipartFile file, String description) throws IncorrectFileExtensionException, IOException {

		String fileContentType = file.getContentType();

		if (contentTypes.contains(fileContentType)) {
			String generatedName = UUID.randomUUID().toString() + '.'
					+ FilenameUtils.getExtension(file.getOriginalFilename());

			Files.copy(file.getInputStream(), this.uploads.resolve(generatedName));
			return postService.createPost(generatedName, description);

		} else {
			throw new IncorrectFileExtensionException("Invalid file extension. Only PNG/JPEG files are allowed");
		}

	}
	
	@Override
	public AvatarResponse saveAvatar(MultipartFile file) throws IncorrectFileExtensionException, IOException {

		String fileContentType = file.getContentType();

		if (contentTypes.contains(fileContentType)) {
			String generatedName = UUID.randomUUID().toString() + '.'
					+ FilenameUtils.getExtension(file.getOriginalFilename());

			Files.copy(file.getInputStream(), this.avatars.resolve(generatedName));
			return userService.setProfilePicture(generatedName);

		} else {
			throw new IncorrectFileExtensionException("Invalid file extension. Only png/jpeg/webp/bmp files are allowed");
		}

	}

	@Override
	public Resource load(String filename) throws MalformedURLException, FileNotFoundException {
		Path file = uploads.resolve(filename);
		return getResource(file.toUri(), filename);
	}
	
	@Override
	public Resource loadAvatar(String filename) throws MalformedURLException, FileNotFoundException {
		Path file = avatars.resolve(filename);
		return getResource(file.toUri(), filename);	
	}
	
	@Override
	public Set<String> listUploadedPhotos() throws IOException {
	    try (var stream = Files.list(uploads)) {
	        return stream
	          .filter(file -> !Files.isDirectory(file))
	          .map(Path::getFileName)
	          .map(Path::toString)
	          .collect(Collectors.toSet());
	    }
	}
	
	@Override
	public Set<String> listUploadedAvatars() throws IOException {
	    try (var stream = Files.list(avatars)) {
	        return stream
	          .filter(file -> !Files.isDirectory(file))
	          .map(Path::getFileName)
	          .map(Path::toString)
	          .collect(Collectors.toSet());
	    }
	}
	
	@Override
	public void deleteUploadedPhoto(String filename) throws IOException {
		Path file = uploads.resolve(filename);
		Files.delete(file);
	}
	
	@Override
	public void deleteUploadedAvatar(String filename) throws IOException {
		Path file = avatars.resolve(filename);
		Files.delete(file);
	}
	
	private Resource getResource(URI uri, String filename) throws FileNotFoundException, MalformedURLException {
		
		Resource resource = new UrlResource(uri);

		if (resource.exists() || resource.isReadable()) {
			return resource;
		} else {
			throw new FileNotFoundException("Optional file " + filename + " was not found.");
		}
		
	}	
}

