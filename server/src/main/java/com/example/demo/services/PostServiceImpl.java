package com.example.demo.services;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exceptions.EntityNotFoundException;
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
	public boolean deletePost(Long id) {
		
		var found = postRepository.findById(id);
		
		if (found.isPresent()) {
			postRepository.delete(found.get());
			return true;
		}
		else {
			throw new EntityNotFoundException(Post.class, "id", id.toString());
		}	
	}
	
	@Override
	public Post getPostByID(Long id) {

		var found = postRepository.findById(id);

		if (found.isPresent())

		{	var post = new Post();
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
		} else {
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
		} else {
			throw new EntityNotFoundException(Post.class, "id", comment.getId().toString());
		}
	}

	@Override
	public void processLike(Long id) {

		var post = postRepository.findById(id);

		if (post.isPresent()) {
			var postt = post.get();
			var user = userService.getUserFromSession();

			if (likeRepository.isLiked(user, postt)) {
				likeRepository.dislike(user, postt);
			}
			else {
				var like = new Like();
				like.setPost(postt);
				like.setUtilisateur(user);
				likeRepository.save(like);
			}
			
		} else {
			throw new EntityNotFoundException(Post.class, "id", id.toString());
		}
	}

	@Override
	public Comment removeComment(Comment comment) {
		// TODO Auto-generated method stub
		return null;
	}
}
