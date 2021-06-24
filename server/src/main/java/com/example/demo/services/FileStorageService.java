package com.example.demo.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exceptions.IncorrectFileExtensionException;
import com.example.demo.persistence.models.Post;

public interface FileStorageService {

	Post save(MultipartFile file, String description) throws IncorrectFileExtensionException, IOException;

	Resource load(String filename) throws MalformedURLException, FileNotFoundException;

	void init() throws IOException;

}
