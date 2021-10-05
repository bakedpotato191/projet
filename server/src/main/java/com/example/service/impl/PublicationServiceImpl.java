package com.example.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.dao.CommentRepository;
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
	
	@Autowired
	private CommentRepository cRepository;
	
	@Autowired
    @Qualifier("mainExecutor") 
    private Executor existingThreadPool; 

	@Override
	@Async
	public CompletableFuture<List<PublicationDto>> getUserPublications(String username, Integer pageNo, Integer pageSize, String sortBy) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		var publications = publicationRepository.findAllByUtilisateurUsername(username, paging);
		
		return publications.thenApplyAsync(pbs -> 
			pbs.stream().map(p -> {
				var pub = mapper.pubToPubDto(p);
				pub.setLiked(userService.isAnonymous() ? false : likeRepository.isLiked(userService.getAuthenticatedUser(), p));
				pub.setCountLike(likeRepository.countByPost(p));
				pub.setCommentsCount(cRepository.countByPost(p));
				pub.setAuthor(userService.isAnonymous() ? false : userService.getAuthenticatedUser().equals(p.getUtilisateur()));
				return pub;
			}).collect(Collectors.toList()), existingThreadPool);
	}
	

	@Override
	@Async
	public CompletableFuture<List<PublicationDto>> getNewPublications(Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("date").descending());
		var publications = publicationRepository.findNewPublications(userService.getAuthenticatedUser().getUsername(), paging);
		
		return publications.thenApplyAsync(pbs -> 
			pbs.stream().map(p -> {
				var pub = mapper.pubToPubDto(p);
				pub.setLiked(likeRepository.isLiked(userService.getAuthenticatedUser(), p));
				pub.setCountLike(likeRepository.countByPost(p));
				pub.setCommentsCount(cRepository.countByPost(p));
				pub.setAuthor(userService.isAnonymous() ? false : userService.getAuthenticatedUser().equals(p.getUtilisateur()));
				return pub;
			}).collect(Collectors.toList()), existingThreadPool);
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
	public PublicationDto getPublicationByID(Long id) {
		var optional = publicationRepository.findById(id);

		if (optional.isPresent()) {
			var publication = optional.get();
			var result = mapper.pubToPubDto(publication);
			result.setLiked(userService.isAnonymous() ? false : likeRepository.isLiked(userService.getAuthenticatedUser(), publication));
			result.setCountLike(publication.getLikes().size());
			result.setCommentsCount(publication.getComments().size());
			result.setAuthor(userService.isAnonymous() ? false : userService.getAuthenticatedUser().equals(publication.getUtilisateur()));
			return result;
		} 
		else {
			throw new EntityNotFoundException(Publication.class, "id", id.toString());
		}
	}
	
	@Async
	@Override
	public void like(Long id) {
		var post = new Publication();
		post.setId(id);		
		var favori = new Favori();
		favori.setPost(post);
		favori.setUtilisateur(userService.getAuthenticatedUser());
		likeRepository.save(favori);
	}

	@Async
	@Override
	public void dislike(Long id) {
			var currentUser = userService.getAuthenticatedUser();
			likeRepository.dislike(currentUser, id);
	}
	
	@Async
	@Override
	public CompletableFuture<List<PublicationDto>> getFavorites(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		var favorites = likeRepository.findAllByUtilisateurUsername(userService.getAuthenticatedUser().getUsername(), paging);
		
		return favorites.thenApplyAsync(favs -> 
			 favs.stream().map(f -> {
				var fav = mapper.pubToPubDto(f.getPost());
				fav.setLiked(likeRepository.isLiked(userService.getAuthenticatedUser(), f.getPost()));
				fav.setCountLike(likeRepository.countByPost(f.getPost()));
				fav.setCommentsCount(cRepository.countByPost(f.getPost()));
				fav.setAuthor(userService.isAnonymous() ? false : userService.getAuthenticatedUser().equals(f.getUtilisateur()));
				return fav;
			}).collect(Collectors.toList()), existingThreadPool);
	}
}
