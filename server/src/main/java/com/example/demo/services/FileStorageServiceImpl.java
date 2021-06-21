package com.example.demo.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exceptions.IncorrectFileExtensionException;
import com.example.demo.mapstruct.mappers.MapStructMapper;
import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.repository.PostRepository;

@Service
@Transactional
public class FileStorageServiceImpl implements FileStorageService {

	private final Path root = Paths.get("uploads");
	private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg");

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MapStructMapper mapstructMapper;

	public void init() {
		try {
			if (!Files.isDirectory(root)) {
				Files.createDirectory(root);
			}
		} catch (IOException e) {
			throw new InternalError("Could not initialize folder for upload!");
		}
	}

	@Override
	public Post save(MultipartFile file, String description) throws IncorrectFileExtensionException, IOException {

		String fileContentType = file.getContentType();

		if (contentTypes.contains(fileContentType)) {
			String generatedName = UUID.randomUUID().toString() + '.'
					+ FilenameUtils.getExtension(file.getOriginalFilename());

			Files.copy(file.getInputStream(), this.root.resolve(generatedName));

			var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			var post = new Post();
			post.setUrl("http://localhost:8081/api/user/photos/" + generatedName);
			post.setDate(new Timestamp(System.currentTimeMillis()));
			post.setdescription(description);
			post.setUtilisateur(mapstructMapper.userImplToUser(user));

			postRepository.save(post);

			return post;
		} else {
			throw new IncorrectFileExtensionException(
					"Invalid file extension. Only PNG/JPEG files are allowed");
		}

	}

	@Override
	public Resource load(String filename) throws MalformedURLException, FileNotFoundException {

			Path file = root.resolve(filename);
			
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new FileNotFoundException("Optional file " + filename + " was not found.");
		}
	}
}
