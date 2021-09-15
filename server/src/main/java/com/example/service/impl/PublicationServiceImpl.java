package com.example.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.dao.FavoriRepository;
import com.example.rest.dao.PublicationRepository;
import com.example.rest.model.Favori;
import com.example.rest.model.Publication;
import com.example.service.PublicationService;
import com.example.service.UserService;
import com.example.web.dto.response.PublicationDto;
import com.example.web.exception.EntityNotFoundException;
import com.example.web.exception.HttpUnauthorizedException;
import com.example.web.mappers.MapstructMapper;

@Service
@Transactional
public class PublicationServiceImpl implements PublicationService {

	@Autowired
	private PublicationRepository publicationRepository;

	@Autowired
	private FavoriRepository likeRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private MapstructMapper mapper;

	@Override
	@Async
	public CompletableFuture<List<PublicationDto>> getUserPublications(String username, Integer pageNo, Integer pageSize, String sortBy) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Slice<Publication> slicedResult = publicationRepository.findAllByUtilisateurUsername(username, paging);
		
		if (slicedResult.hasContent()) {
			
			List<PublicationDto> list = new ArrayList<>();
			Iterator<Publication> it = slicedResult.iterator();
			
			while(it.hasNext()) {
				Publication p = it.next();
				var result = mapper.pubToPubDto(p);
				result.setCountLike(p.getLikes().size());
				result.setCommentsCount(p.getComments().size());
				list.add(result);
			}
			return CompletableFuture.completedFuture(list);
		}
		else {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}

	}
	
	@Override
	@Async
	public CompletableFuture<List<PublicationDto>> getNewPublications(Integer pageNo, Integer pageSize) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Slice<Publication> slicedResult = publicationRepository.findNewPublications(userService.getAuthenticatedUser().getUsername(), paging);
		
		if (slicedResult.hasContent()) {
			List<PublicationDto> list = new ArrayList<>();
			Iterator<Publication> it = slicedResult.iterator();
			
			while(it.hasNext()) {
				Publication p = it.next();
				var result = mapper.pubToPubDto(p);
				result.setLiked(likeRepository.isLiked(userService.getAuthenticatedUser(), p));
				result.setCountLike(p.getLikes().size());
				result.setCommentsCount(p.getComments().size());
				list.add(result);
			}
			return CompletableFuture.completedFuture(list);
		}
		else {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}
	}

	@Override
	public void deletePublication(Long id) {
		
		if (publicationRepository.deleteByIdAndUtilisateur(id, userService.getAuthenticatedUser()) == 0) {
			throw new HttpUnauthorizedException("La ressource n'existe pas ou vous n'avez pas la permission de la modifier");
		}	
	}

	@Override
	public Publication createPost(String generatedName, String description) {
		var post = new Publication();
		post.setPhoto("http://localhost:8081/api/publication/view/" + generatedName);
		post.setDate(new Timestamp(System.currentTimeMillis()));
		post.setDescription(description);
		post.setUtilisateur(userService.getAuthenticatedUser());
		return publicationRepository.save(post);
	}
	
	@Override
	@Async
	public CompletableFuture<PublicationDto> getPublicationByID(Long id) {
		var optional = publicationRepository.findById(id);

		if (optional.isPresent()) {
			var publication = optional.get();
			var result = mapper.pubToPubDto(publication);
			result.setLiked(userService.isAnonymous() ? false : likeRepository.isLiked(userService.getAuthenticatedUser(), publication));
			result.setCountLike(publication.getLikes().size());
			result.setCommentsCount(publication.getComments().size());
			
			return CompletableFuture.completedFuture(result);
		} 
		else {
			throw new EntityNotFoundException(Publication.class, "id", id.toString());
		}
	}
	
	@Override
	@Async
	public void like(Long id) {
		var post = new Publication();
		post.setId(id);		
		var favori = new Favori();
		favori.setPost(post);
		favori.setUtilisateur(userService.getAuthenticatedUser());
		likeRepository.save(favori);
	}

	@Override
	@Async
	public void dislike(Long id) {
			var currentUser = userService.getAuthenticatedUser();
			likeRepository.dislike(currentUser, id);
	}
	
	@Override
	@Async
	public CompletableFuture<List<PublicationDto>> getFavorites(Integer pageNo, Integer pageSize, String sortBy) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Slice<Favori> slicedResult = likeRepository.findAllByUtilisateur(userService.getAuthenticatedUser(), paging);
		
		if (slicedResult.hasContent()) {
		
		List<PublicationDto> list = new ArrayList<>();
		Iterator<Favori> it = slicedResult.iterator();
		
		while(it.hasNext()) {
			Publication p = it.next().getPost();
			var result = mapper.pubToPubDto(p);
			result.setCountLike(p.getLikes().size());
			result.setCommentsCount(p.getComments().size());
			list.add(result);
		}
		return CompletableFuture.completedFuture(list);
	}
		else {
			return CompletableFuture.completedFuture(new ArrayList<>());
		}
	}
}
