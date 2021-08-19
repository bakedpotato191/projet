package com.example.demo.rest.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.rest.exceptions.EntityNotFoundException;
import com.example.demo.rest.exceptions.HttpUnauthorizedException;
import com.example.demo.rest.models.Like;
import com.example.demo.rest.models.Post;
import com.example.demo.rest.repository.LikeRepository;
import com.example.demo.rest.repository.PostRepository;
import com.example.demo.rest.services.PostService;
import com.example.demo.rest.services.UserService;

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
		var user = userService.getUserFromSession();
		var post = new Post();
		post.setUrl("http://localhost:8081/api/post/view/" + generatedName);
		post.setDate(new Timestamp(System.currentTimeMillis()));
		post.setDescription(description);
		post.setUtilisateur(user);
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
			post.setUrl(found.get().getUrl());
			post.setUtilisateur(found.get().getUtilisateur());
			boolean liked = likeRepository.isLiked(userService.getUserFromSession(), found.get());
			post.setLiked(liked);
			
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
