package com.example.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.rest.model.Publication;
import com.example.web.dto.response.AvatarResponse;
import com.example.web.exception.IncorrectFileExtensionException;

public interface FileStorageService {

	Publication save(MultipartFile file, String description) throws IncorrectFileExtensionException, IOException;

	Resource load(String filename) throws MalformedURLException, FileNotFoundException;

	void init() throws IOException;

	CompletableFuture<AvatarResponse> saveAvatar(MultipartFile file) throws IncorrectFileExtensionException, IOException;

	Resource loadAvatar(String filename) throws MalformedURLException, FileNotFoundException;

	Set<String> listUploadedPhotos() throws IOException;

	Set<String> listUploadedAvatars() throws IOException;

	void deleteUploadedPhoto(String filename) throws IOException;

	void deleteUploadedAvatar(String filename) throws IOException;
}
