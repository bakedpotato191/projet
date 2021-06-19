package com.example.demo.security.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.mapstruct.mappers.MapStructMapper;
import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.repository.PostRepository;

@Service
public class FilesStorageServiceImpl implements FileStorageService {

	private final Path root = Paths.get("uploads");
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private MapStructMapper mapstructMapper;

	@Override
	public void init() {
		try {
			 if (!Files.isDirectory(root)){
				Files.createDirectory(root);
			 }
		} catch (IOException e) {
			throw new InternalError("Could not initialize folder for upload!");
		}
	}

	@Override
	public void save(MultipartFile file, String description) {
		try {
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
		} catch (Exception e) {
			throw new InternalError("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new InternalError("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new InternalError("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new InternalError("Could not load the files!");
		}
	}
}
