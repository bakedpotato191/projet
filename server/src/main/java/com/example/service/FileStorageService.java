package com.example.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.web.dto.response.AvatarResponse;
import com.example.web.exception.IncorrectFileExtensionException;

public interface FileStorageService {

	void init() throws IOException;
	
	void save(MultipartFile file, String description) throws IncorrectFileExtensionException, IOException;

	CompletableFuture<AvatarResponse> saveAvatar(MultipartFile file) throws IncorrectFileExtensionException, IOException;

	Resource loadAvatar(String filename) throws MalformedURLException, FileNotFoundException;

	Resource loadPhoto(String filename) throws MalformedURLException, FileNotFoundException;

	void deleteUnusedPhotos() throws IOException;

	void deleteUnusedAvatars() throws IOException;
}
