package com.example.task;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.service.FileStorageService;

@Service
public class FileRemovalTask {

	private static final Logger log = LoggerFactory.getLogger(FileRemovalTask.class);

	@Autowired
	private FileStorageService storageService;

	@Scheduled(cron = "${cron.expression.photos}", zone="Europe/Paris")
	public void purgeUnusedPhotos() throws IOException {
		log.info("Scheduled removal of unused photos");
		storageService.deleteUnusedPhotos();
		log.info("The removal of unused photos has been completed");
	}

	@Scheduled(cron = "${cron.expression.avatars}", zone="Europe/Paris")
	public void purgeUnusedAvatars() throws IOException {
		log.info("Scheduled removal of unused avatars");
		storageService.deleteUnusedPhotos();
		log.info("The removal of unused avatars has been completed");
	}
}
