package com.example.demo.rest.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.rest.exceptions.EntityNotFoundException;
import com.example.demo.rest.models.Follower;
import com.example.demo.rest.models.User;
import com.example.demo.rest.repository.FollowerRepository;
import com.example.demo.rest.repository.PostRepository;
import com.example.demo.rest.repository.UserRepository;
import com.example.demo.rest.services.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String USERNAME = "username";
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;

    @Autowired
    private FollowerRepository followerRepository;

	@Override
	public User getUserData(String username){
		
		var user = userRepository.findByUsername(username);
		
		if(user.isPresent())
		{	
			var result = user.get();
			result.setPostCount(postRepository.countByUtilisateur(user.get()));
			result.setFollowed(followerRepository.isFollowed(getUserFromSession(), result));
			result.setFollowerCount(followerRepository.countFollowers(username));
			result.setFollowingCount(followerRepository.countFollowing(username));
			return result;
        }
		else {
			throw new EntityNotFoundException(User.class, USERNAME, username);
		}
	}
	
	@Override
	public void follow(String username) {
		
		var user = userRepository.findByUsername(username);
		
		if (user.isPresent()) {
			var currentUser = getUserFromSession();
			var following = user.get();
			followerRepository.save(new Follower(currentUser, following));
		}
		else {
			throw new EntityNotFoundException(User.class, USERNAME, username);
		}
	}
	
	@Override
	public void unfollow(String username) {
		
		int row = followerRepository.unfollow(getUserFromSession(), username);
		
		if (row == 0) {
			throw new EntityNotFoundException(User.class, USERNAME, username);
		}
	}
	
	@Override
	public User getUserFromSession() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
