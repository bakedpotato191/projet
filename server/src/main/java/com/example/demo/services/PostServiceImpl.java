package com.example.demo.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.HttpUnauthorizedException;
import com.example.demo.mapstruct.dto.CommentDto;
import com.example.demo.persistence.models.Comment;
import com.example.demo.persistence.models.Like;
import com.example.demo.persistence.models.Post;
import com.example.demo.persistence.repository.CommentRepository;
import com.example.demo.persistence.repository.LikeRepository;
import com.example.demo.persistence.repository.PostRepository;

@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

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
			post.setComments(commentRepository.findAllByPostOrderByDateDesc(post));
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
	public Comment addComment(final CommentDto comment) {

		var post = postRepository.findById(comment.getId());

		if (post.isPresent()) {
			
			var comm = new Comment();
			comm.setUtilisateur(userService.getUserFromSession());
			comm.setText(comment.getText());
			comm.setDate(new Timestamp(System.currentTimeMillis()));
			comm.setPost(post.get());
			
			return commentRepository.save(comm);
		} 
		else {
			throw new EntityNotFoundException(Post.class, "id", comment.getId().toString());
		}
	}

	@Override
	public void likeThePost(Long id) {
		
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
	public void dislikeThePost(Long id) {
		
			var currentUser = userService.getUserFromSession();
			
			if (likeRepository.dislike(currentUser, id) == 0) {
				throw new EntityNotFoundException(Post.class, "id", id.toString());
			}
	}
	
	@Override
	public Comment removeComment(Comment comment) {
		return null;
	}
	
	@Override
	public List<Post> getUserPosts(String username) {
		return postRepository.findAllByUtilisateurUsernameOrderByDateDesc(username);
	}
	
	@Override
	public Set<Like> getFavorites() {
		return likeRepository.findAllByUtilisateurOrderByPostDateDesc(userService.getUserFromSession());
	}
}
