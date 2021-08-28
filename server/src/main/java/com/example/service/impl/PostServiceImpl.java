package com.example.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rest.dao.LikeRepository;
import com.example.rest.dao.PostRepository;
import com.example.rest.model.Like;
import com.example.rest.model.Post;
import com.example.service.PostService;
import com.example.service.UserService;
import com.example.web.exception.EntityNotFoundException;
import com.example.web.exception.HttpUnauthorizedException;

@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private UserService userService;

	//

	@Override
	public Post createPost(String generatedName, String description) {
		var post = new Post();
		post.setPhoto("http://localhost:8081/api/post/view/" + generatedName);
		post.setDate(new Timestamp(System.currentTimeMillis()));
		post.setDescription(description);
		post.setUtilisateur(userService.getUserFromSession());
		return postRepository.save(post);
	}

	@Override
	public void deletePost(Long id) {
		
		var user = userService.getUserFromSession();
		
		if (postRepository.deleteByIdAndUtilisateur(id, user) == 0) {
			throw new HttpUnauthorizedException("Resource doesn't exist/You do not have the permission to modify it");
		}	
	}

	@Override
	public Post getPostByID(Long id) {
		var found = postRepository.findById(id);

		if (found.isPresent()) {
			var post = new Post();
			post.setId(found.get().getId());
			post.setDescription(found.get().getDescription());
			post.setCountLike(likeRepository.countByPost(found.get()));
			post.setDate(found.get().getDate());
			post.setPhoto(found.get().getPhoto());
			post.setUtilisateur(found.get().getUtilisateur());
			
			if (SecurityContextHolder.getContext().getAuthentication() 
			          instanceof AnonymousAuthenticationToken) {
				post.setLiked(false);
			}
			else {
				post.setLiked(likeRepository.isLiked(userService.getUserFromSession(), found.get()));
			}
			
			return post;
		} 
		else {
			throw new EntityNotFoundException(Post.class, "id", id.toString());
		}
	}

	@Override
	public void like(Long id) {
		
		var post = new Post();
		post.setId(id);		

		var like = new Like();
		like.setPost(post);
		like.setUtilisateur(userService.getUserFromSession());
		
		try {
			likeRepository.save(like);
		}
		catch (IllegalArgumentException ex) {
			throw new EntityNotFoundException(Post.class, "id", id.toString());
		}

	}

	@Override
	public void dislike(Long id) {
		
			var currentUser = userService.getUserFromSession();
			
			if (likeRepository.dislike(currentUser, id) == 0) {
				throw new EntityNotFoundException(Post.class, "id", id.toString());
			}
	}
	
	@Override
	public List<Post> getUserPosts(String username, Integer pageNo, Integer pageSize, String sortBy) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Slice<Post> slicedResult = postRepository.findAllByUtilisateurUsername(username, paging);
		
		if (slicedResult.hasContent()) {
			slicedResult.forEach(post -> 
			post.setCountLike(likeRepository.countByPost(post)));
			return slicedResult.getContent();
		}
		else {
			return new ArrayList<>();
		}

	}
	
	@Override
	public List<Like> getFavorites(Integer pageNo, Integer pageSize, String sortBy) {
		
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Slice<Like> slicedResult = likeRepository.findAllByUtilisateur(userService.getUserFromSession(), paging);
		
		if (slicedResult.hasContent()) {
			return slicedResult.getContent();
		}
		else {
			return new ArrayList<>();
		}
	}
}
