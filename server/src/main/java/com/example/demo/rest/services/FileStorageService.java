package com.example.demo.rest.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.rest.exceptions.IncorrectFileExtensionException;
import com.example.demo.rest.models.Post;

public interface FileStorageService {

	Post save(MultipartFile file, String description) throws IncorrectFileExtensionException, IOException;

	Resource load(String filename) throws MalformedURLException, FileNotFoundException;

	void init() throws IOException;

	String saveAvatar(MultipartFile file) throws IncorrectFileExtensionException, IOException;

	Resource loadAvatar(String filename) throws MalformedURLException, FileNotFoundException;
}
